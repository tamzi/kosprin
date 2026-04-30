rootProject.name = "kosprin"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

listOf(
    "analytics-service",
    "common",
    "feed-service",
    "gateway",
    "metadata-service",
    "notification-service",
    "search-service",
    "video-service"
).forEach { include(it) }
