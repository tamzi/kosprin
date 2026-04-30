plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSpring)
    alias(libs.plugins.kotlinJpa)
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":common"))
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterActuator)
    implementation(libs.springBootStarterDataJpa)
    implementation(libs.springBootStarterOauth2ResourceServer)
    implementation(libs.springKafka)
    implementation(libs.flyway)
    implementation(libs.springdocOpenApi)
    implementation(libs.kotlinReflect)
    implementation(libs.jacksonModuleKotlin)
    runtimeOnly(libs.postgresql)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springKafkaTest)
}

tasks.test {
    useJUnitPlatform()
}
