plugins {
    alias(libs.plugins.kotlinJvm)
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.kotlinReflect)
    implementation(libs.jacksonModuleKotlin)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
