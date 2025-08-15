package id.dev.home.presentation.camera.component

import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import id.dev.core.presentation.utils.UiText
import id.dev.home.presentation.R
import id.dev.home.presentation.model.BarcodeResult
import id.dev.home.presentation.utils.mapBarcodeToResult

@Composable
fun CameraPreview(
    onQrCodeScanned: (BarcodeResult?) -> Unit,
    boundingBox: Rect? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var lastScannedValue by remember { mutableStateOf<BarcodeResult?>(null) }
    var lastScanTime by remember { mutableLongStateOf(0L) }
    val scanCooldown = 2000L

    val options = remember {
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    }
    val scanner = remember { BarcodeScanning.getClient(options) }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            imageAnalysisBackpressureStrategy = ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
        }
    }


    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            PreviewView(it).apply {
                this.controller = controller
            }
        },
        update = { previewView ->
            controller.bindToLifecycle(lifecycleOwner)
            controller.setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                MlKitAnalyzer(
                    listOf(scanner),
                    ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
                    ContextCompat.getMainExecutor(context)
                ) { result ->
                    try {
                        val barcodes: List<Barcode> = result.getValue(scanner).orEmpty()
                        if (barcodes.isEmpty()) return@MlKitAnalyzer

                        for (barcode in barcodes) {
                            if (boundingBox != null && !isBarcodeInBoundingBox(
                                    barcode,
                                    boundingBox,
                                )
                            ) {
                                continue
                            }
                            val result = barcode.mapBarcodeToResult()

                            if ((result != lastScannedValue || System.currentTimeMillis() - lastScanTime > scanCooldown)
                            ) {
                                lastScannedValue = result
                                lastScanTime = System.currentTimeMillis()
                                result?.let {
                                    onQrCodeScanned(result)
                                } ?: onQrCodeScanned(
                                    BarcodeResult.ScanError(
                                        UiText.StringResource(
                                            R.string.no_qr_codes
                                        )
                                    )
                                )
                            }
                        }
                    } catch (t: Throwable) {
                        onQrCodeScanned(
                            BarcodeResult.ScanError(
                                UiText.StringResource(
                                    R.string.analyzer_error,
                                    arrayOf(t.message ?: "Unknown")
                                )
                            )
                        )
                    }
                }
            )
        }
    )
}

private fun isBarcodeInBoundingBox(
    barcode: Barcode,
    boundingBox: Rect,
): Boolean {
    val barcodeBounds = barcode.boundingBox ?: return false

    return android.graphics.Rect(
        boundingBox.left.toInt(),
        boundingBox.top.toInt(),
        boundingBox.right.toInt(),
        boundingBox.bottom.toInt()
    ).contains(barcodeBounds)
}

