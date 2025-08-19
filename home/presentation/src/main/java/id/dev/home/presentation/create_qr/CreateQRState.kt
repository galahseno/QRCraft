package id.dev.home.presentation.create_qr

data class CreateQRState(
    val availableTypes: List<QRType> = emptyList(),
    val selectedType: QRType? = null,
    )