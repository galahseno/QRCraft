package id.dev.home.presentation.utils

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import id.dev.core.presentation.R
import id.dev.core.presentation.utils.UiText
import id.dev.home.presentation.model.QrTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class QrCodeAnalyzer(private val context: Context) {
    
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()
    
    private val scanner = BarcodeScanning.getClient(options)
    
    suspend fun analyzeImage(uri: Uri): QrTypes? = withContext(Dispatchers.IO) {
        try {
            val inputImage = InputImage.fromFilePath(context, uri)
            val barcodes = scanner.process(inputImage).await()
            
            if (barcodes.isEmpty()) {
                return@withContext QrTypes.Error(
                    UiText.StringResource(R.string.no_qr_codes_found_in_image)
                )
            }

            return@withContext barcodes.firstOrNull()?.mapBarcodeToResult()
            
        } catch (e: Exception) {
            Timber.e("Error analyzing image: ${e.message}")
            return@withContext QrTypes.Error(
                UiText.StringResource(
                    0,
                    arrayOf(e.message ?: "Unknown")
                )
            )
        }
    }
}