package id.dev.home.presentation.scan_result

sealed interface ScanResultScreenAction {
    data object OnNavigateUpClicked: ScanResultScreenAction
    data class OnTitleChanged(val title: String): ScanResultScreenAction
}