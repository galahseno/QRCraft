package id.dev.qrcraft.navigation.screens

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screens {

    @Serializable
    data object CameraScreen

    @Serializable
    data class ScanResultScreen(
        val barcodeResult: String
    )

    @Serializable
    data class CreateQrScreen(
        val qrType: String
    )

    @Serializable
    data object GenerateQrScreen

    @Serializable
    data object HistoryQrScreen
}