package id.dev.home.presentation.camera

import id.dev.home.presentation.model.QrTypes

sealed interface CameraScreenAction {
    data class SubmitCameraPermissionInfo(
        val acceptedCameraPermission: Boolean,
        val showCameraRationale: Boolean
    ) : CameraScreenAction

    data class OnScanResult(val result: QrTypes?) : CameraScreenAction
    data object OnDismissErrorDialog : CameraScreenAction
}