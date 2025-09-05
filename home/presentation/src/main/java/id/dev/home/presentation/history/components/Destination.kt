package id.dev.home.presentation.history.components

enum class Destination(
    val route: String,
    val label: String,
) {
    SCANNED(route = "Scanned_history_screen", label = "Scanned"),
    GENERATED(route = "Generated_history_screen", label = "Generated")
}