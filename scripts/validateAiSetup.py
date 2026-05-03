#!/usr/bin/env python3
"""Validate the repo-owned AI setup."""

from __future__ import annotations

import re
import sys
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
AI_ROOT = ROOT / ".ai"

ENTRY_TYPES = {
    "rules": "rule",
    "skills": "skill",
    "agents": "agent",
    "workflows": "workflow",
    "memory": "memory",
}

WRAPPERS = [
    ROOT / "AGENTS.md",
    ROOT / "CLAUDE.md",
    ROOT / ".cursor/rules/main.mdc",
    ROOT / ".github/copilot-instructions.md",
]

MARKDOWN_LINK_RE = re.compile(r"\[[^\]]+\]\(([^)]+)\)")
LOWER_CAMEL_MD_RE = re.compile(r"^[a-z][a-zA-Z0-9]*\.md$")
ADR_MD_RE = re.compile(r"^\d{4}-[a-z0-9]+(?:-[a-z0-9]+)*\.md$")


def parse_frontmatter(path: Path) -> dict[str, str]:
    text = path.read_text(encoding="utf-8")
    if not text.startswith("---\n"):
        return {}

    end = text.find("\n---\n", 4)
    if end == -1:
        return {}

    fields: dict[str, str] = {}
    for line in text[4:end].splitlines():
        if ":" not in line:
            continue
        key, value = line.split(":", 1)
        fields[key.strip()] = value.strip()
    return fields


def parse_uses(value: str) -> list[str]:
    if not value:
        return []
    value = value.strip()
    if value.startswith("[") and value.endswith("]"):
        value = value[1:-1]
    return [item.strip() for item in value.split(",") if item.strip()]


def repo_path_for_link(source: Path, raw: str) -> Path | None:
    target = raw.split("#", 1)[0].strip()
    if not target or target.startswith(("#", "http://", "https://", "mailto:")):
        return None
    if target.startswith("mdc:"):
        return ROOT / target[4:].lstrip("/")
    if target.startswith("/"):
        return ROOT / target.lstrip("/")
    return (source.parent / target).resolve()


def should_skip_path(path: Path) -> bool:
    skip_parts = {".git", ".gradle", "build", "node_modules", ".qodo"}
    return any(part in skip_parts for part in path.relative_to(ROOT).parts)


def validate_frontmatter(errors: list[str]) -> dict[str, Path]:
    entries: dict[str, Path] = {}

    for dirname, expected_type in ENTRY_TYPES.items():
        for path in sorted((AI_ROOT / dirname).glob("*.md")):
            fields = parse_frontmatter(path)
            rel = path.relative_to(ROOT)
            missing = [key for key in ("name", "description", "type") if key not in fields]
            if missing:
                errors.append(f"{rel}: missing frontmatter fields: {', '.join(missing)}")
                continue

            if fields["type"] != expected_type:
                errors.append(f"{rel}: type is {fields['type']!r}, expected {expected_type!r}")

            if path.name != "README.md" and fields["name"] != path.stem:
                errors.append(f"{rel}: name {fields['name']!r} must match filename stem {path.stem!r}")

            entries[fields["name"]] = path
            entries[path.stem] = path

    return entries


def validate_uses(entries: dict[str, Path], errors: list[str]) -> None:
    for dirname in ENTRY_TYPES:
        for path in sorted((AI_ROOT / dirname).glob("*.md")):
            fields = parse_frontmatter(path)
            for used in parse_uses(fields.get("uses", "")):
                if used not in entries:
                    errors.append(f"{path.relative_to(ROOT)}: unknown uses entry {used!r}")


def validate_links(errors: list[str]) -> None:
    files = list(AI_ROOT.rglob("*.md"))
    files += [path for path in WRAPPERS if path.exists()]
    files += list((ROOT / "docs").rglob("*.md"))
    files.append(ROOT / "jiraboard.md")

    for path in sorted(set(files)):
        if should_skip_path(path) or not path.exists():
            continue
        text = path.read_text(encoding="utf-8")
        for match in MARKDOWN_LINK_RE.finditer(text):
            raw = match.group(1)
            target = repo_path_for_link(path, raw)
            if target is None:
                continue
            if not target.exists():
                line = text[: match.start()].count("\n") + 1
                errors.append(f"{path.relative_to(ROOT)}:{line}: broken link -> {raw}")


def validate_markdown_filenames(errors: list[str]) -> None:
    allowed_exact = {
        Path("AGENTS.md"),
        Path("CLAUDE.md"),
        Path(".github/copilot-instructions.md"),
    }

    for path in sorted(ROOT.rglob("*.md")):
        if should_skip_path(path):
            continue
        rel = path.relative_to(ROOT)
        if path.name == "README.md" or rel in allowed_exact:
            continue
        if rel.parts[:2] == ("docs", "adr") and ADR_MD_RE.match(path.name):
            continue
        if not LOWER_CAMEL_MD_RE.match(path.name):
            errors.append(f"{rel}: markdown filename must be camelCase or a documented exception")


def validate_readme_lengths(errors: list[str]) -> None:
    for path in sorted(ROOT.rglob("README.md")):
        if should_skip_path(path):
            continue
        if path == ROOT / "README.md":
            continue
        lines = path.read_text(encoding="utf-8").splitlines()
        if len(lines) > 100:
            errors.append(f"{path.relative_to(ROOT)}: README is {len(lines)} lines, max 100")


def validate_wrappers(errors: list[str]) -> None:
    required_terms = [
        "AGENTS.md",
        ".ai",
        "Never push",
        "--no-verify",
        "--force",
        "Atomic commits",
        "One",
        "Module README",
        "No copied source-code snippets",
        "camelCase",
        "Secrets never",
        "common/",
    ]

    for path in WRAPPERS:
        if not path.exists():
            errors.append(f"{path.relative_to(ROOT)}: wrapper is missing")
            continue
        text = path.read_text(encoding="utf-8")
        for term in required_terms:
            if term not in text:
                errors.append(f"{path.relative_to(ROOT)}: wrapper missing required term {term!r}")
        stale_agent_link = re.search(r"\]\((?:\.\./)?agents/|\]\(/agents/|mdc:agents/", text)
        if stale_agent_link:
            errors.append(f"{path.relative_to(ROOT)}: stale agents/ reference; use .ai/")


def main() -> int:
    errors: list[str] = []

    entries = validate_frontmatter(errors)
    validate_uses(entries, errors)
    validate_links(errors)
    validate_markdown_filenames(errors)
    validate_readme_lengths(errors)
    validate_wrappers(errors)

    if errors:
        print("AI setup validation failed:", file=sys.stderr)
        for error in errors:
            print(f"- {error}", file=sys.stderr)
        return 1

    print("AI setup validation passed.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
