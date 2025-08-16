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
    data object CreateQrScreen

    @Serializable
    data object HistoryQrScreen
}