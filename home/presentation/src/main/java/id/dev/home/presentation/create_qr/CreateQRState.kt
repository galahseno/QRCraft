package id.dev.home.presentation.create_qr

import id.dev.home.presentation.create_qr.model.QRType

data class CreateQRState(
    val availableTypes: List<QRType> = emptyList(),
)