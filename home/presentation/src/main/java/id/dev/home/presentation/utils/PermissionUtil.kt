package id.dev.home.presentation.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import id.dev.home.presentation.camera.CameraScreenAction

fun ComponentActivity.checkCameraPermissionAndRationale(onAction: (CameraScreenAction) -> Unit) {
    val isGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    val showCameraRationale = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)

    onAction(
        CameraScreenAction.SubmitCameraPermissionInfo(
            acceptedCameraPermission = isGranted,
            showCameraRationale = showCameraRationale
        )
    )
}
