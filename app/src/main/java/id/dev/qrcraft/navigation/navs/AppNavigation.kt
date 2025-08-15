package id.dev.qrcraft.navigation.navs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import id.dev.home.presentation.camera.CameraScreenRoot
import id.dev.home.presentation.model.BarcodeResult
import id.dev.home.presentation.scanResult.ScanResultScreenRoot
import id.dev.qrcraft.navigation.screens.Screens
import kotlinx.serialization.json.Json

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screens.CameraScreen
    ) {
        composable<Screens.CameraScreen> {
            CameraScreenRoot(
                onScanResult = { barcodeResult, ->
                    if (barcodeResult !is BarcodeResult.ScanError) {
                        val result = Json.encodeToString(BarcodeResult.serializer(), barcodeResult)
                        navController.navigate(
                            route = Screens.ScanResultScreen(
                                barcodeResult = result
                            )
                        )
                    }
                }
            )
        }
        composable<Screens.ScanResultScreen> {
            ScanResultScreenRoot(
                onNavigateUp = {
                    navController.navigateUp()
                },
            )
        }
    }
}