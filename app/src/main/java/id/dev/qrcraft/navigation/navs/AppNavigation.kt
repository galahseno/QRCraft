package id.dev.qrcraft.navigation.navs

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import id.dev.home.presentation.camera.CameraScreenRoot
import id.dev.home.presentation.model.BarcodeResult
import id.dev.home.presentation.scanResult.ScanResultScreenRoot
import id.dev.home.presentation.utils.copyToClipboard
import id.dev.home.presentation.utils.share
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
        composable<Screens.ScanResultScreen> {

            val context = LocalContext.current
            val args = it.toRoute<Screens.ScanResultScreen>()

            ScanResultScreenRoot(
                barcodeResult = args.barcodeResult,
                onNavigateUp = {
                    navController.navigateUp()
                },
                onOpenLink = { url ->
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    navController.context.startActivity(intent)
                },
                onShare = { content ->
                    share(
                        context = context,
                        link = content
                    )
                },
                onCopyClicked = { content ->
                    context.copyToClipboard(content)
                }
            )
        }
    }
}