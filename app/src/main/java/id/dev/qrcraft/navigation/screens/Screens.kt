package id.dev.qrcraft.navigation.screens

import kotlinx.serialization.Serializable

@Serializable
sealed class Screens(open val route: String) {

    @Serializable
    data object CameraScreen : Screens(route = "camera_screen")

    @Serializable
    data class ScanResultScreen(
        val qrTypes: String,
        val titleVal: String
    ) : Screens("scan_result_screen") {
        override val route: String
            get() = "scan_result_screen/$qrTypes/$titleVal"

        companion object {
            const val ROUTE_PATTERN = "scan_result_screen/{qrTypes}/{titleVal}"
        }
    }

    @Serializable
    data object CreateQrScreen : Screens("create_qr_screen")

    @Serializable
    data class GenerateQrScreen(
        val qrType: String
    ) : Screens("generate_qr_screen") {
        override val route: String
            get() = "generate_qr_screen/$qrType"

        companion object {
            const val ROUTE_PATTERN = "generate_qr_screen/{qrType}"
        }
    }

    @Serializable
    data object HistoryQrScreen : Screens("history_qr_screen")
}