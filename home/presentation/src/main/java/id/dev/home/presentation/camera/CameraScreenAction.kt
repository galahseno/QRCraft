package id.dev.home.presentation.camera

import id.dev.home.presentation.model.BarcodeResult

sealed interface CameraScreenAction {
    data class SubmitCameraPermissionInfo(
        val acceptedCameraPermission: Boolean,
        val showCameraRationale: Boolean
    ) : CameraScreenAction

    data class OnScanResult(val result: BarcodeResult?) : CameraScreenAction
    data object OnDismissErrorDialog : CameraScreenAction
}