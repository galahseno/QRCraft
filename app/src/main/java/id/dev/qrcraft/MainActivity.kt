package id.dev.qrcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import id.dev.core.presentation.theme.QRCraftTheme
import id.dev.qrcraft.navigation.navs.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            QRCraftTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}