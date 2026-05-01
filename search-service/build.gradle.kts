plugins {
    id("kosprin.kafka-consumer")
}

dependencies {
    implementation(project(":common"))
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterDataElasticsearch)
    implementation(libs.springBootStarterDataRedis)
    implementation(libs.springBootStarterOauth2ResourceServer)
    implementation(libs.springdocOpenApi)
}
