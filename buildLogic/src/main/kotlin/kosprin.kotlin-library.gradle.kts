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

dependencies {
    "testImplementation"(platform(libs.findLibrary("junitBom").get()))
    "testImplementation"(libs.findLibrary("junitJupiter").get())
}

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

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        html.required = true
    }
}
