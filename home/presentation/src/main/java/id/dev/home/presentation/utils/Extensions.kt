package id.dev.home.presentation.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import com.google.mlkit.vision.barcode.common.Barcode
import id.dev.home.presentation.model.BarcodeResult

fun Barcode.mapBarcodeToResult(): BarcodeResult? {
    return when (valueType) {
        Barcode.TYPE_CONTACT_INFO -> {
            val contact = contactInfo
            val name = contact?.name?.formattedName.orEmpty()
            val email = contact?.emails?.first()?.address.orEmpty()
            val phone = contact?.phones?.first()?.number.orEmpty()
            BarcodeResult.Contact(name, email, phone)
        }

        Barcode.TYPE_GEO -> {
            val geo = geoPoint
            if (geo != null) {
                BarcodeResult.Geo(geo.lat, geo.lng)
            } else null
        }

        Barcode.TYPE_PHONE -> {
            val phone = phone
            if (phone != null) {
                BarcodeResult.Phone(phone.number.orEmpty())
            } else null
        }

        Barcode.TYPE_URL -> {
            val url = url
            if (url != null) {
                BarcodeResult.Link(url.url.orEmpty())
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

                BarcodeResult.Wifi(
                    wifi.ssid.orEmpty(),
                    wifi.password.orEmpty(),
                    encryptionTypeString
                )
            } else null
        }

        Barcode.TYPE_TEXT -> {
            BarcodeResult.Text(rawValue.orEmpty())
        }

        else -> null
    }
}

fun Context.copyToClipboard(text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)
}

fun share(context: Context, link: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, link)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}