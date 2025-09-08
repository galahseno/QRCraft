package id.dev.home.presentation.create_qr.model

import id.dev.core.presentation.R
import id.dev.home.presentation.model.QrTypeIdentifier

enum class CreateQrTypes(val title: Int, val icon: Int, val identifier: QrTypeIdentifier) {
    TEXT(R.string.text_qr_code, R.drawable.text_ic, QrTypeIdentifier.TEXT),
    LINK(R.string.link_qr_code, R.drawable.link_ic, QrTypeIdentifier.LINK),
    CONTACT(R.string.contact_qr_code, R.drawable.contact_ic, QrTypeIdentifier.CONTACT),
    PHONE(R.string.phone_qr_code, R.drawable.phone_ic, QrTypeIdentifier.PHONE),
    GEO(R.string.location_qr_code, R.drawable.geo_ic, QrTypeIdentifier.GEO),
    WIFI(R.string.wi_fi_qr_code, R.drawable.wi_fi_ic, QrTypeIdentifier.WIFI);

    companion object Companion {
        fun fromString(type: String): CreateQrTypes? {
            return entries.find { it.name.equals(type, ignoreCase = true) }
        }
    }
}