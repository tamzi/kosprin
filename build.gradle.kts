plugins {
    alias(libs.plugins.springBoot) apply false
    alias(libs.plugins.springDependencyManagement) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinSpring) apply false
    alias(libs.plugins.kotlinJpa) apply false
    alias(libs.plugins.benManesVersions)
}

allprojects {
    group = "com.kosprin"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

tasks.named("dependencyUpdates", com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask::class.java) {
    rejectVersionIf {
        val nonStable = listOf("alpha", "beta", "rc", "m", "preview", "snapshot")
        nonStable.any { candidate.version.lowercase().contains(it) }
    }
}
