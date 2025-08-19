package id.dev.home.presentation.create_qr

import id.dev.core.presentation.R
import id.dev.core.presentation.utils.UiText
import id.dev.home.presentation.model.BarcodeResult
import kotlinx.serialization.Serializable

@Serializable
sealed class QrCodeTypes {
    @Serializable
    data class Contact(val name: String, val email: String, val phone: String) : QrCodeTypes()
    @Serializable
    data class Geo(val lat: Double, val lng: Double) : QrCodeTypes()
    @Serializable
    data class Phone(val number: String) : QrCodeTypes()
    @Serializable
    data class Link(val url: String) : QrCodeTypes()
    @Serializable
    data class Text(val content: String) : QrCodeTypes()
    @Serializable
    data class Wifi(val ssid: String, val password: String, val encryptionType: String) : QrCodeTypes()
}


@Serializable
enum class QrTypeIdentifier {
    TEXT, LINK, CONTACT, PHONE, GEO, WIFI
}

@Serializable
data class QRType(
    val title: String,
    val icon: Int,
    val identifier: QrTypeIdentifier // Add this field
)

// 3. Update your available types list
val availableTypes = listOf(
    QRType(
        title = "Text",
        icon = R.drawable.text_ic,
        identifier = QrTypeIdentifier.TEXT
    ),
    QRType(
        title = "Link",
        icon = R.drawable.link_ic,
        identifier = QrTypeIdentifier.LINK
    ),
    QRType(
        title = "Contact",
        icon = R.drawable.contact_ic,
        identifier = QrTypeIdentifier.CONTACT
    ),
    QRType(
        title = "Phone Number",
        icon = R.drawable.phone_ic,
        identifier = QrTypeIdentifier.PHONE
    ),
    QRType(
        title = "Geolocation",
        icon = R.drawable.geo_ic,
        identifier = QrTypeIdentifier.GEO
    ),
    QRType(
        title = "Wi-Fi",
        icon = R.drawable.wi_fi_ic,
        identifier = QrTypeIdentifier.WIFI
    )
)