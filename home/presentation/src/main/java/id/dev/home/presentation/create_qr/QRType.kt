package id.dev.home.presentation.create_qr

import id.dev.core.presentation.R
import kotlinx.serialization.Serializable

@Serializable
enum class QrTypeIdentifier { TEXT, LINK, CONTACT, PHONE, GEO, WIFI }

data class QRType(
    val title: String,
    val icon: Int,
    val identifier: QrTypeIdentifier
)

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