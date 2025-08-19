package id.dev.home.presentation.create_qr

sealed interface CreateQREvent {
    data class NavigateToQRGenerator(val qrTypeIdentifier: QrTypeIdentifier) : CreateQREvent
}