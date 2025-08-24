package id.dev.qrcraft.navigation.screens

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screens {

    @Serializable
    data object CameraScreen : Screens

    @Serializable
    data class ScanResultScreen(
        val qrTypes: String,
        val titleVal: String
    ) : Screens

    @Serializable
    data object CreateQrScreen : Screens

    @Serializable
    data class GenerateQrScreen(
        val qrType: String
    ) : Screens

    @Serializable
    data object HistoryQrScreen : Screens
}