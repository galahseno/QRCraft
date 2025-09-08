package id.dev.home.presentation.create_qr

import id.dev.home.presentation.create_qr.model.CreateQrTypes

data class CreateQRState(
    val availableTypes: List<CreateQrTypes> = emptyList(),
)