plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSpring)
}

kotlin {
    jvmToolchain(21)
}

dependencyManagement {
    imports {
        mavenBom(libs.springCloudDependencies.get().toString())
    }
}

dependencies {
    implementation(project(":common"))
    implementation(libs.springCloudStarterGateway)
    implementation(libs.springBootStarterActuator)
    implementation(libs.springBootStarterOauth2ResourceServer)
    implementation(libs.kotlinReflect)
    implementation(libs.jacksonModuleKotlin)
    testImplementation(libs.springBootStarterTest)
}

tasks.test {
    useJUnitPlatform()
}
