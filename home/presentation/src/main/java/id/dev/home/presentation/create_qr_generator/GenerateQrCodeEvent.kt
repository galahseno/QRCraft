package id.dev.home.presentation.create_qr_generator

sealed interface GenerateQrCodeEvent {
    data class GenerateQrCode(val data: String) : GenerateQrCodeEvent
    data object NavigateBack : GenerateQrCodeEvent
}