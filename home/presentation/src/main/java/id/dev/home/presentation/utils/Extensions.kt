package id.dev.home.presentation.utils

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import id.dev.home.presentation.camera.CameraScreenAction
import id.dev.home.presentation.model.QrTypes

fun ComponentActivity.checkCameraPermissionAndRationale(onAction: (CameraScreenAction) -> Unit) {
    val isGranted = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
    val showCameraRationale = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)

    onAction(
        CameraScreenAction.SubmitCameraPermissionInfo(
            acceptedCameraPermission = isGranted,
            showCameraRationale = showCameraRationale
        )
    )
}

fun Barcode.mapBarcodeToResult(): QrTypes? {
    return when (valueType) {
        Barcode.TYPE_CONTACT_INFO -> {
            val contact = contactInfo
            val name = contact?.name?.formattedName.orEmpty()
            val email = contact?.emails?.first()?.address.orEmpty()
            val phone = contact?.phones?.first()?.number.orEmpty()
            QrTypes.Contact(name, email, phone)
        }

        Barcode.TYPE_GEO -> {
            val geo = geoPoint
            if (geo != null) {
                QrTypes.Geo(geo.lat, geo.lng)
            } else null
        }

        Barcode.TYPE_PHONE -> {
            val phone = phone
            if (phone != null) {
                QrTypes.Phone(phone.number.orEmpty())
            } else null
        }

        Barcode.TYPE_URL -> {
            val url = url
            if (url != null) {
                QrTypes.Link(url.url.orEmpty())
            } else null
        }

        Barcode.TYPE_WIFI -> {
            val wifi = wifi
            if (wifi != null) {
                val encryptionTypeString = when (wifi.encryptionType) {
                    Barcode.WiFi.TYPE_OPEN -> "OPEN"
                    Barcode.WiFi.TYPE_WPA -> "WPA"
                    Barcode.WiFi.TYPE_WEP -> "WEP"
                    else -> "UNKNOWN"
                }

                QrTypes.Wifi(
                    wifi.ssid.orEmpty(),
                    wifi.password.orEmpty(),
                    encryptionTypeString
                )
            } else null
        }

        Barcode.TYPE_TEXT -> {
            QrTypes.Text(rawValue.orEmpty())
        }

        else -> null
    }
}

fun Context.copyToClipboard(text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)
}

fun Context.share(link: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, link)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

fun generateQrBitmap(
    text: String,
    size: Int = 1024,
    margin: Int = 1,
    ecLevel: ErrorCorrectionLevel = ErrorCorrectionLevel.M,
    foreground: Int = Color.BLACK,
    background: Int = Color.WHITE,
): Bitmap {
    val hints = hashMapOf(
        EncodeHintType.CHARACTER_SET to "UTF-8",
        EncodeHintType.MARGIN to margin,
        EncodeHintType.ERROR_CORRECTION to ecLevel
    )

    val matrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints)
    val bmp = createBitmap(size, size)

    val pixels = IntArray(size * size)
    var offset = 0
    for (y in 0 until size) {
        for (x in 0 until size) {
            pixels[offset + x] = if (matrix[x, y]) foreground else background
        }
        offset += size
    }
    bmp.setPixels(pixels, 0, size, 0, 0, size, size)
    return bmp
}

fun isValidPhoneNumber(phoneNumber: String): Boolean = Patterns.PHONE.matcher(phoneNumber).matches()

fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

// this function was taken from internet. If you have better approach, feel free to modify it.
fun isValidLatLng(latitude: Double, longitude: Double): Boolean {
    // Check latitude validity
    if (latitude < -90.0 || latitude > 90.0) {
        return false
    }
    // Check longitude validity
    if (longitude < -180.0 || longitude > 180.0) {
        return false
    }
    // If both are within range, the coordinates are considered valid
    return true
}