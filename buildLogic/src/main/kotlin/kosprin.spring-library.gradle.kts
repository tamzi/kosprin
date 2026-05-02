plugins {
    id("kosprin.kotlin-library")
    kotlin("plugin.spring")
}

val libs = the<org.gradle.api.artifacts.VersionCatalogsExtension>().named("libs")

dependencies {
    "implementation"(platform(libs.findLibrary("springBootDependencies").get()))
    "compileOnly"(libs.findLibrary("springBootAutoconfigure").get())
    "compileOnly"("org.springframework.boot:spring-boot-starter-web")
    "compileOnly"(libs.findLibrary("springKafka").get())
}
