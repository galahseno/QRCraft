package id.dev.qrcraft.navigation.navs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screens.CameraScreen,
        modifier = Modifier.consumeWindowInsets(contentPadding)
    ) {
        composable<Screens.CameraScreen> {
            CameraScreenRoot(
                onScanResult = { barcodeResult ->
                    if (barcodeResult !is QrTypes.Error) {
                        val result = Json.encodeToString(QrTypes.serializer(), barcodeResult)
                        navController.navigate(
                            route = Screens.ScanResultScreen(
                                qrTypes = result,
                                titleVal = context.getString(R.string.scan_result)
                            )
                        )
                    }
                },
            )
        }
        composable<Screens.ScanResultScreen> { backStackEntry ->
            ScanResultScreenRoot(
                onNavigateUp = {
                    navController.navigateUp()
                },
            )
        }
        composable<Screens.CreateQrScreen> {
            CreateQRRoot(
                onNavigateToGenerator = { qrTypeIdentifier ->
                    val result =
                        Json.encodeToString(QrTypeIdentifier.serializer(), qrTypeIdentifier)
                    navController.navigate(
                        route = Screens.GenerateQrScreen(
                            qrType = result
                        )
                    )
                }
            )
        }
        composable<Screens.GenerateQrScreen> { backStackEntry ->
            CreateQrGeneratorRoot(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToPreview = { qr ->
                    navController.navigate(
                        route = Screens.ScanResultScreen(
                            qrTypes = qr,
                            titleVal = context.getString(R.string.preview)
                        )
                    )
                }
            )
        }
    }
}