package id.dev.home.presentation.camera

import android.net.Uri
import androidx.camera.view.LifecycleCameraController
import id.dev.home.presentation.model.QrTypes

sealed interface CameraScreenAction {

    data class SubmitCameraPermissionInfo(
        val acceptedCameraPermission: Boolean,
        val showCameraRationale: Boolean
    ) : CameraScreenAction

    data class OnScanResult(val result: QrTypes?) : CameraScreenAction

    data class OnCameraControllerReady(val controller: LifecycleCameraController) : CameraScreenAction
    data class OnTorchStateChanged(val torchState: Int) : CameraScreenAction
    data class OnFlashlightAvailabilityChanged(val hasFlashlight: Boolean) : CameraScreenAction

    data object OnDismissErrorDialog : CameraScreenAction
    data object OnFlashlightClicked: CameraScreenAction
    data class OnImageSelected(val uri: Uri?) : CameraScreenAction
    object OnGalleryClicked : CameraScreenAction
}