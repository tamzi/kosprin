plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSpring)
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":common"))
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterActuator)
    implementation(libs.springBootStarterDataElasticsearch)
    implementation(libs.springBootStarterDataRedis)
    implementation(libs.springBootStarterOauth2ResourceServer)
    implementation(libs.springKafka)
    implementation(libs.springdocOpenApi)
    implementation(libs.kotlinReflect)
    implementation(libs.jacksonModuleKotlin)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springKafkaTest)
}

tasks.test {
    useJUnitPlatform()
}
