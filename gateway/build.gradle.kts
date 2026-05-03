plugins {
    id("kosprin.spring-service")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloud.get()}")
    }
}

dependencies {
    implementation(project(":common"))
    implementation(libs.springCloudStarterGateway)
    implementation(libs.springBootStarterOauth2ResourceServer)
}
