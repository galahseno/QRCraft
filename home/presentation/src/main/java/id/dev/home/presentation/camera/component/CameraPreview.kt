package id.dev.home.presentation.camera.component

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import id.dev.core.presentation.utils.DeviceConfiguration
import id.dev.core.presentation.utils.UiText
import id.dev.home.presentation.R
import id.dev.home.presentation.model.BarcodeResult
import id.dev.home.presentation.utils.QrCodeScanner
import timber.log.Timber

@Composable
fun CameraPreview(
    onQrCodeScanned: (BarcodeResult?) -> Unit,
    boundingBox: Rect? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val isLandscape = when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT, DeviceConfiguration.TABLET_PORTRAIT -> false
        else -> true
    }

    val previewView = remember { PreviewView(context) }

    LaunchedEffect(Unit) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()

        val preview = Preview.Builder().build().apply {
            surfaceProvider = previewView.surfaceProvider
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .apply {
                setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    QrCodeScanner(
                        onQrCodeScanned = onQrCodeScanned,
                        boundingBox = boundingBox,
                        isLandscape = isLandscape
                    )
                )
            }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (exc: Exception) {
            Timber.e(exc, "CameraPreview error: ${exc.message}")
            exc.message?.let {
                onQrCodeScanned(
                    BarcodeResult.ScanError(
                        UiText.StringResource(
                            R.string.initialize_error,
                            arrayOf(it)
                        )
                    )
                )
            }
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { previewView }
    )
}

