package id.dev.qrcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import id.dev.core.presentation.theme.QRCraftTheme
import id.dev.home.presentation.camera.CameraScreenRoot
import id.dev.home.presentation.model.BarcodeResult
import kotlinx.serialization.json.Json
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            QRCraftTheme {
                CameraScreenRoot(
                    onScanResult = { barcodeResult ->
                        // exclude the scan error since it will not reach until here
                        if (barcodeResult !is BarcodeResult.ScanError) {
                            //encode
                            val result = Json.encodeToString(BarcodeResult.serializer(), barcodeResult)
                            Timber.tag("ScanResult encode").d(result)
                            //decode
                            val decodedResult = Json.decodeFromString(BarcodeResult.serializer(), result)
                            Timber.tag("ScanResult decode").d("$decodedResult")
                        }
                    }
                )
            }
        }
    }
}