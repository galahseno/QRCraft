package id.dev.home.presentation.scanResult

sealed interface ScanResultScreenAction {

    data object OnNavigateUpClicked: ScanResultScreenAction

    data class OnShareClicked(val shareContent: String) : ScanResultScreenAction

    data class OnLinkClicked(val link: String) : ScanResultScreenAction

    data class OnCopyClicked(val copyContent: String): ScanResultScreenAction

}