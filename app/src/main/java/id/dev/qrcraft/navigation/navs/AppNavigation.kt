package id.dev.qrcraft.navigation.navs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import id.dev.core.presentation.R
import id.dev.home.presentation.camera.CameraScreenRoot
import id.dev.home.presentation.create_qr.CreateQRRoot
import id.dev.home.presentation.create_qr.QrTypeIdentifier
import id.dev.home.presentation.create_qr_generator.CreateQrGeneratorRoot
import id.dev.home.presentation.model.QrTypes
import id.dev.home.presentation.scanResult.ScanResultScreenRoot
import id.dev.qrcraft.navigation.screens.Screens
import kotlinx.serialization.json.Json

@Composable
fun AppNavigation(
    navController: NavHostController,
    contentPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screens.CameraScreen.route,
        modifier = Modifier.consumeWindowInsets(contentPadding)
    ) {
        composable(Screens.CameraScreen.route) {
            CameraScreenRoot(
                onScanResult = { barcodeResult ->
                    if (barcodeResult !is QrTypes.Error) {
                        val result = Json.encodeToString(QrTypes.serializer(), barcodeResult)
                        navController.navigate(
                            route = Screens.ScanResultScreen(
                                qrTypes = result,
                                titleVal = "Scan Result"
                            ).route
                        )
                    }
                },
            )
        }
        composable(
            route = Screens.ScanResultScreen.ROUTE_PATTERN,
            arguments = listOf(
                navArgument("qrTypes") { type = NavType.StringType },
                navArgument("titleVal") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ScanResultScreenRoot(
                onNavigateUp = {
                    navController.navigateUp()
                },
            )
        }
        composable(Screens.CreateQrScreen.route) {
            CreateQRRoot(
                onNavigateToGenerator = { qrTypeIdentifier ->
                    val result = Json.encodeToString(QrTypeIdentifier.serializer(), qrTypeIdentifier)
                    navController.navigate(
                        route = Screens.GenerateQrScreen(
                            qrType = result
                        ).route
                    )
                }
            )
        }
        composable(
            route = Screens.GenerateQrScreen.ROUTE_PATTERN,
            arguments = listOf(
                navArgument("qrType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            CreateQrGeneratorRoot(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToPreview = { qr ->
                    navController.navigate(
                        route = Screens.ScanResultScreen(
                            qrTypes = qr,
                            titleVal = "Preview"
                        ).route
                    )
                }
            )
        }
    }
}