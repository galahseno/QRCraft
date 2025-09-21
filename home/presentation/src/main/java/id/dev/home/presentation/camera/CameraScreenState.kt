package id.dev.home.presentation.camera

import android.net.Uri
import androidx.camera.view.LifecycleCameraController
import id.dev.core.presentation.utils.UiText

data class CameraScreenState(
    val hasCameraPermission: Boolean = false,
    val showScreenRationale: Boolean = false,
    val isLoading: Boolean = false,
    val isScanError: Boolean = false,
    val errorMessage: UiText? = null,
    val hasFlashlight: Boolean = false,
    val isFlashlightOn: Boolean = false,
    val cameraController: LifecycleCameraController? = null,
    val isProcessingImage: Boolean = false,
    val selectedImageUri: Uri? = null
)