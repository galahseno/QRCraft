package id.dev.home.presentation.create_qr_generator

import android.webkit.URLUtil.isValidUrl
import id.dev.home.presentation.create_qr.QrTypeIdentifier
import id.dev.home.presentation.model.QrTypes
import id.dev.home.presentation.utils.isValidEmail
import id.dev.home.presentation.utils.isValidLatLng
import id.dev.home.presentation.utils.isValidPhoneNumber
import kotlinx.serialization.json.Json

data class GenerateQrScreenState(
    val qrTypeIdentifier: QrTypeIdentifier? = null,
    val textInput: String = "",
    val urlInput: String = "",
    val contactName: String = "",
    val contactEmail: String = "",
    val contactPhone: String = "",
    val phoneNumber: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val wifiSSID: String = "",
    val wifiPassword: String = "",
    val wifiEncryption: String = "",
    val generatedQrCode: String? = null,
    val isLoading: Boolean = false,
) {
    val isWifiFormValid: Boolean
        get() = wifiSSID.isNotBlank() &&
                wifiPassword.isNotBlank() &&
                wifiEncryption.isNotBlank()

    val isContactFormValid: Boolean
        get() = contactName.isNotBlank() &&
                isValidEmail(contactEmail) &&
                isValidPhoneNumber(contactPhone)

    val isUrlFormValid: Boolean
        get() = isValidUrl(urlInput)

    val isTextFormValid: Boolean
        get() = textInput.isNotBlank()

    val isLocationFormValid: Boolean
        get() = longitude.toDoubleOrNull()?.let { lng ->
            latitude.toDoubleOrNull()?.let { lat ->
                isValidLatLng(
                    latitude = lat,
                    longitude = lng
                )
            }
        } == true

    val isPhoneNumberFormValid: Boolean
        get() = isValidPhoneNumber(phoneNumber)

    val qrContent: String?
        get() = generatedQrCode?.let { qrString ->
            try {
                val qrData = Json.decodeFromString<QrTypes>(qrString)
                when (qrData) {
                    is QrTypes.Text -> qrData.content
                    is QrTypes.Link -> qrData.url
                    is QrTypes.Contact -> "${qrData.name}\n${qrData.email}\n${qrData.phone}"
                    is QrTypes.Phone -> qrData.number
                    is QrTypes.Geo -> "${qrData.lat}, ${qrData.lng}"
                    is QrTypes.Wifi -> "SSID: ${qrData.ssid}"
                    is QrTypes.Error -> qrData.message
                }
            } catch (e: Exception) {
                null
            }.toString()
        }
}