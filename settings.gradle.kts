rootProject.name = "kosprin"

listOf(
    "analytics-service",
    "common",
    "feed-service",
    "dashboard",
    "gateway",
    "metadata-service",
    "notification-service",
    "search-service",
    "video-service"
).forEach { include(it) }
