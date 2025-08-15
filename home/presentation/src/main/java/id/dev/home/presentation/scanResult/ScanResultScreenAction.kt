package id.dev.home.presentation.scanResult

sealed interface ScanResultScreenAction {
    data object OnNavigateUpClicked: ScanResultScreenAction
}