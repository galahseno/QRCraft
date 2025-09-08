package id.dev.home.presentation.model

import id.dev.core.presentation.utils.UiText

sealed class QrTypes {
    data class Contact(val name: String, val email: String, val phone: String) : QrTypes()
    data class Geo(val lat: Double, val lng: Double) : QrTypes()
    data class Phone(val number: String) : QrTypes()
    data class Link(val url: String) : QrTypes()
    data class Text(val content: String) : QrTypes()
    data class Wifi(val ssid: String, val password: String, val encryptionType: String) : QrTypes()
    data class Error(val message: UiText) : QrTypes()
}

fun QrTypes.getTitle(): String = when (this) {
    is QrTypes.Contact -> "Contact"
    is QrTypes.Geo -> "Geo"
    is QrTypes.Phone -> "Phone"
    is QrTypes.Link -> "Link"
    is QrTypes.Text -> "Text"
    is QrTypes.Wifi -> "WiFi"
    is QrTypes.Error -> "Error"
}

fun QrTypes.getContent(): String = when (this) {
    is QrTypes.Contact -> "${name}\n${email}\n${phone}"
    is QrTypes.Geo -> "$lat, $lng"
    is QrTypes.Phone -> number
    is QrTypes.Link -> url
    is QrTypes.Text -> content
    is QrTypes.Wifi -> "SSID: $ssid\nPassword: $password\nEncryption: $encryptionType"
    is QrTypes.Error -> message.toString() // Already exclude in view model logic
}