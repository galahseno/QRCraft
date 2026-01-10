package id.dev.home.presentation.create_qr

import id.dev.home.presentation.model.QrTypeIdentifier

sealed interface CreateQRAction {
    data class SelectQRType(val qrTypeIdentifier: QrTypeIdentifier): CreateQRAction
    data object CreateQrShortcut: CreateQRAction
}