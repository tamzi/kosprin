plugins {
    id("kosprin.kotlin-library")
}

dependencies {
    implementation(libs.kotlinReflect)
    implementation(libs.jacksonModuleKotlin)
    testImplementation(kotlin("test"))
}
