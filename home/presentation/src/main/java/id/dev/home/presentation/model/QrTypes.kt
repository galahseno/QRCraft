package id.dev.home.presentation.model

import id.dev.core.presentation.utils.UiText
import kotlinx.serialization.Serializable

@Serializable
sealed class QrTypes {
    @Serializable
    data class Contact(val name: String, val email: String, val phone: String) : QrTypes()
    @Serializable
    data class Geo(val lat: Double, val lng: Double) : QrTypes()
    @Serializable
    data class Phone(val number: String) : QrTypes()
    @Serializable
    data class Link(val url: String) : QrTypes()
    @Serializable
    data class Text(val content: String) : QrTypes()
    @Serializable
    data class Wifi(val ssid: String, val password: String, val encryptionType: String) : QrTypes()
    @Serializable
    data class Error(val message: UiText) : QrTypes()
}