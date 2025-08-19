package id.dev.home.presentation.create_qr_generator

import id.dev.home.presentation.create_qr.QrTypeIdentifier

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
    val wifiEncryption: String = "WPA",
    val generatedQrCode: String? = null,
    val isLoading: Boolean = false
)