package id.dev.qrcraft.navigation.navs

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import id.dev.home.presentation.camera.CameraScreenRoot
import id.dev.home.presentation.camera.CameraScreenViewModel
import id.dev.home.presentation.model.BarcodeResult
import id.dev.home.presentation.scanResult.ScanResultScreen
import id.dev.qrcraft.navigation.screens.Screens
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screens.CameraScreen
    ) {
        composable<Screens.CameraScreen> {
            val viewModel: CameraScreenViewModel = koinViewModel()
            viewModel.state.collectAsStateWithLifecycle().value.let { state ->
                CameraScreenRoot(
                    onScanResult = { barcodeResult ->
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
        }
        composable<Screens.ScanResultScreen> {
            ScanResultScreen(
                navController = navController,
                barcodeResult = it.arguments?.getString("barcodeResult") ?: ""
            )
        }
    }
}