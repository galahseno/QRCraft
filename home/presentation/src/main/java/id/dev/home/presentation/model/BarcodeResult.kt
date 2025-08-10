package id.dev.home.presentation.model

import id.dev.core.presentation.utils.UiText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class BarcodeResult {
    @Serializable
    @SerialName("contact")
    data class Contact(val name: String, val email: String, val phone: String) : BarcodeResult()
    @Serializable
    @SerialName("geo")
    data class Geo(val lat: Double, val lng: Double) : BarcodeResult()
    @Serializable
    @SerialName("phone")
    data class Phone(val number: String) : BarcodeResult()
    @Serializable
    @SerialName("link")
    data class Link(val url: String) : BarcodeResult()
    @Serializable
    @SerialName("text")
    data class Text(val content: String) : BarcodeResult()
    @Serializable
    @SerialName("wifi")
    data class Wifi(val ssid: String, val password: String, val encryptionType: String) : BarcodeResult()

    @Serializable
    @SerialName("scanError")
    data class ScanError(val message: UiText) : BarcodeResult()
}