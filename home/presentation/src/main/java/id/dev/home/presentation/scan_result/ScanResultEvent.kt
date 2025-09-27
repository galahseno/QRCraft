package id.dev.home.presentation.scan_result

sealed interface ScanResultEvent {
    data object SaveImageSuccess : ScanResultEvent
    data class SaveImageError(val message: String) : ScanResultEvent
}