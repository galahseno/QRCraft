package id.dev.home.presentation.camera

import id.dev.core.presentation.utils.UiText

data class CameraScreenState(
    val hasCameraPermission: Boolean = false,
    val showScreenRationale: Boolean = false,
    val isLoading: Boolean = false,
    val isScanError: Boolean = false,
    val errorMessage: UiText? = null
)