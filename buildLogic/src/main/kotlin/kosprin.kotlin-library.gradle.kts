plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    jacoco
}

val libs = the<org.gradle.api.artifacts.VersionCatalogsExtension>().named("libs")

kotlin {
    jvmToolchain(21)
}

// ── Unit test dependencies ────────────────────────────────────────────────────

dependencies {
    "testImplementation"(platform(libs.findLibrary("junitBom").get()))
    "testImplementation"(libs.findLibrary("junitJupiter").get())
    "testImplementation"(libs.findLibrary("mockk").get())
    "testImplementation"(libs.findLibrary("kotestAssertionsCore").get())
    "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
}

// ── Integration test source set ───────────────────────────────────────────────

val integrationTest by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += output + compileClasspath
}

configurations["integrationTestImplementation"].extendsFrom(configurations["testImplementation"])
configurations["integrationTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

dependencies {
    "integrationTestImplementation"(libs.findLibrary("testcontainersCore").get())
    "integrationTestImplementation"(libs.findLibrary("testcontainersJunit").get())
}

val integrationTestTask = tasks.register<Test>("integrationTest") {
    description = "Runs integration tests"
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    useJUnitPlatform()
    shouldRunAfter(tasks.test)
    finalizedBy(tasks.jacocoTestReport)
}

tasks.check { dependsOn(integrationTestTask) }

// ── Static analysis ───────────────────────────────────────────────────────────

detekt {
    buildUponDefaultConfig = true
    allRules = false
}

ktlint {
    version.set("1.3.1")
    filter {
        exclude { it.file.path.contains("/build/") }
    }
}

// ── Coverage ──────────────────────────────────────────────────────────────────

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test, integrationTestTask)
    executionData.setFrom(
        fileTree(layout.buildDirectory).include("jacoco/*.exec"),
    )
    reports {
        xml.required = true
        html.required = true
    }
}

tasks.named("jacocoTestCoverageVerification", JacocoCoverageVerification::class.java) {
    dependsOn(tasks.jacocoTestReport)
    executionData.setFrom(
        fileTree(layout.buildDirectory).include("jacoco/*.exec"),
    )
    onlyIf { tasks.test.get().didWork }
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.70".toBigDecimal()
            }
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.60".toBigDecimal()
            }
        }
    }
}

tasks.check { dependsOn("jacocoTestCoverageVerification") }
