package id.dev.home.presentation.create_qr_generator

sealed interface GenerateQrCodeAction {
    data object OnNavigateUpClicked : GenerateQrCodeAction
    data object OnGenerateQrIsClicked : GenerateQrCodeAction

    // Input actions for different types
    data class OnTextChanged(val text: String) : GenerateQrCodeAction
    data class OnUrlChanged(val url: String) : GenerateQrCodeAction
    data class OnContactNameChanged(val name: String) : GenerateQrCodeAction
    data class OnContactEmailChanged(val email: String) : GenerateQrCodeAction
    data class OnContactPhoneChanged(val phone: String) : GenerateQrCodeAction
    data class OnPhoneNumberChanged(val phone: String) : GenerateQrCodeAction
    data class OnLatitudeChanged(val lat: String) : GenerateQrCodeAction
    data class OnLongitudeChanged(val lng: String) : GenerateQrCodeAction
    data class OnWifiSSIDChanged(val ssid: String) : GenerateQrCodeAction
    data class OnWifiPasswordChanged(val password: String) : GenerateQrCodeAction
    data class OnWifiEncryptionChanged(val encryption: String) : GenerateQrCodeAction
}