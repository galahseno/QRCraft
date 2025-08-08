package id.dev.home.presentation.utils

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toComposeRect
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import id.dev.core.presentation.utils.UiText
import id.dev.home.presentation.R
import id.dev.home.presentation.model.BarcodeResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import kotlin.getValue

class QrCodeScanner(
    private val onQrCodeScanned: (BarcodeResult?) -> Unit,
    private val boundingBox: Rect? = null,
    private val isLandscape: Boolean = false,
) : ImageAnalysis.Analyzer, KoinComponent {
    private val barcodeScanner by inject<BarcodeScanner>()

    private var lastScannedValue: BarcodeResult? = null
    private var lastScanTime = 0L
    private val scanCooldown = 2000

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    if (boundingBox != null && !isBarcodeInBoundingBox(
                            barcode,
                            boundingBox,
                            isLandscape,
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
                        }
                            ?: onQrCodeScanned(BarcodeResult.ScanError(UiText.StringResource(R.string.no_qr_codes)))
                    }
                }
            }
            .addOnFailureListener {
                it.message?.let { errorMessage ->
                    onQrCodeScanned(
                        BarcodeResult.ScanError(
                            UiText.StringResource(
                                R.string.analyzer_error,
                                arrayOf(errorMessage)
                            )
                        )
                    )
                }
                    ?: onQrCodeScanned(BarcodeResult.ScanError(UiText.StringResource(R.string.no_qr_codes)))
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}

fun isBarcodeInBoundingBox(
    barcode: Barcode,
    boundingBox: Rect,
    isLandscape: Boolean
): Boolean {
    val barcodeBounds = barcode.boundingBox?.toComposeRect() ?: return false

    if (barcodeBounds.left >= boundingBox.left || barcodeBounds.top >= boundingBox.top) {
        Timber.tag("BarcodeAnalyzer")
            .d("Barcode bounds: ${barcodeBounds.topLeft}, ${barcodeBounds.topRight}, ${barcodeBounds.bottomRight}, ${barcodeBounds.bottomLeft}")
        Timber.tag("BarcodeAnalyzer")
            .d("Real bounds: ${boundingBox.topLeft}, ${boundingBox.topRight}, ${boundingBox.bottomRight}, ${boundingBox.bottomLeft}")
    }

    // TODO Adjust the bounds check & Handle landscape orientation
    return if (!isLandscape) {
        barcodeBounds.left >= boundingBox.left
    } else {
        barcodeBounds.top >= boundingBox.top
    }
}

