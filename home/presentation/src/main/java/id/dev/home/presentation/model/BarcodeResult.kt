package id.dev.home.presentation.model

import id.dev.core.presentation.utils.UiText
import kotlinx.serialization.Serializable

@Serializable
sealed class BarcodeResult {
    @Serializable
    data class Contact(val name: String, val email: String, val phone: String) : BarcodeResult()
    @Serializable
    data class Geo(val lat: Double, val lng: Double) : BarcodeResult()
    @Serializable
    data class Phone(val number: String) : BarcodeResult()
    @Serializable
    data class Link(val url: String) : BarcodeResult()
    @Serializable
    data class Text(val content: String) : BarcodeResult()
    @Serializable
    data class Wifi(val ssid: String, val password: String, val encryptionType: String) : BarcodeResult()

    @Serializable
    data class ScanError(val message: UiText) : BarcodeResult()
}