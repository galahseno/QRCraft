package id.dev.home.presentation.create_qr_generator

import android.webkit.URLUtil.isValidUrl
import id.dev.core.presentation.utils.UiText
import id.dev.home.presentation.model.QrTypeIdentifier
import id.dev.home.presentation.utils.isValidEmail
import id.dev.home.presentation.utils.isValidLatLng
import id.dev.home.presentation.utils.isValidPhoneNumber

data class GenerateQrScreenState(
    val qrTypeIdentifier: QrTypeIdentifier = QrTypeIdentifier.TEXT,
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
    val isGenerateError: Boolean = false,
    val errorMessage: UiText? = null
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
}