package id.dev.home.presentation.camera

sealed interface CameraScreenEvent {
    data class ScanResult(val qrId: Long) : CameraScreenEvent
}