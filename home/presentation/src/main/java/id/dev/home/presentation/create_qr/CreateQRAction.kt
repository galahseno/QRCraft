package id.dev.home.presentation.create_qr

sealed interface CreateQRAction {
    data class SelectQRType(val typeIdentifier: QrTypeIdentifier): CreateQRAction
}