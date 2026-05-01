plugins {
    id("kosprin.kafka-consumer")
    alias(libs.plugins.kotlinJpa)
}

dependencies {
    implementation(project(":common"))
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterDataJpa)
    implementation(libs.springBootStarterDataRedis)
    implementation(libs.springBootStarterOauth2ResourceServer)
    implementation(libs.flyway)
    implementation(libs.springdocOpenApi)
    runtimeOnly(libs.postgresql)
}
