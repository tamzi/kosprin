plugins {
    alias(libs.plugins.springBoot) apply false
    alias(libs.plugins.springDependencyManagement) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinSpring) apply false
    alias(libs.plugins.kotlinJpa) apply false
}

allprojects {
    group = "com.kosprin"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
