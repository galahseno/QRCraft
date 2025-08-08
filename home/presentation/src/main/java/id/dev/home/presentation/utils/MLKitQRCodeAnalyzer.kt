package id.dev.home.presentation.utils

import android.content.Context
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.PreviewView
import androidx.compose.ui.geometry.Rect
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode
import id.dev.core.presentation.utils.UiText
import id.dev.home.presentation.R
import id.dev.home.presentation.model.BarcodeResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

/**
 * This is exploration for using MLKit Vision from androidX.camera (still can't get the result)
 */
class MLKitQRCodeAnalyzer(
    private val onQrCodeScanned: (BarcodeResult?) -> Unit,
    private val boundingBox: Rect? = null
) : ImageAnalysis.Analyzer, KoinComponent {
    private val context: Context by inject<Context>()
    private val barcodeScanner by inject<BarcodeScanner>()

    private var mlKitAnalyzer: MlKitAnalyzer? = null

    private var lastScannedValue: BarcodeResult? = null
    private var lastScanTime = 0L
    private val scanCooldown = 2000L

    fun getAnalyzer(): MlKitAnalyzer {
        mlKitAnalyzer?.let { return it }

        val analyzer = MlKitAnalyzer(
            listOf(barcodeScanner),
            ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
            ContextCompat.getMainExecutor(context)
        ) { resultValue ->
            Timber.tag("ScanResult").d("$resultValue")
            if (resultValue == null) {
                onQrCodeScanned(
                    BarcodeResult.ScanError(
                        UiText.StringResource(R.string.analyzer_error)
                    )
                )
                return@MlKitAnalyzer
            }

            val barcodes: List<Barcode>? = resultValue.getValue(barcodeScanner)

            if (barcodes.isNullOrEmpty()) {
                onQrCodeScanned(
                    BarcodeResult.ScanError(
                        UiText.StringResource(R.string.no_qr_codes)
                    )
                )
                return@MlKitAnalyzer
            }

            val filteredBarcodes = boundingBox?.let { box ->
                barcodes.filter { barcode ->
                    isBarcodeInBoundingBox(
                        barcode,
                        box,
                        isLandscape = false,
                    )
                }
            } ?: barcodes


            if (filteredBarcodes.isEmpty()) {
                onQrCodeScanned(
                    BarcodeResult.ScanError(
                        UiText.StringResource(R.string.no_qr_codes)
                    )
                )
                return@MlKitAnalyzer
            }

            val result = filteredBarcodes.first().mapBarcodeToResult()

            if (result != lastScannedValue || System.currentTimeMillis() - lastScanTime > scanCooldown) {
                lastScannedValue = result
                lastScanTime = System.currentTimeMillis()
                result?.let { onQrCodeScanned(it) }
                    ?: onQrCodeScanned(
                        BarcodeResult.ScanError(
                            UiText.StringResource(R.string.no_qr_codes)
                        )
                    )
            }
        }

        mlKitAnalyzer = analyzer
        return analyzer
    }

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val analyzer = getAnalyzer()
        try {
            analyzer.analyze(imageProxy)
        } finally {
            imageProxy.close()
        }
    }
}
