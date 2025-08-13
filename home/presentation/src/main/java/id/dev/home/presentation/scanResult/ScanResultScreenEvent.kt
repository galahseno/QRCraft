package id.dev.home.presentation.scanResult

sealed interface ScanResultScreenEvent {
    data object NavigateUp : ScanResultScreenEvent
    data class OpenLink(val url: String) : ScanResultScreenEvent
    data class ShareContent(val share: String) : ScanResultScreenEvent
    data class CopyContent(val copy: String) : ScanResultScreenEvent
}