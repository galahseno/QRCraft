package id.dev.qrcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import id.dev.core.presentation.theme.QRCraftTheme
import id.dev.qrcraft.navigation.components.BottomNavBar
import id.dev.qrcraft.navigation.navs.AppNavigation
import id.dev.qrcraft.navigation.screens.Screens

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            QRCraftTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
                val currentRoute = navBackStackEntry?.destination?.route ?: Screens.CameraScreen.toString()

                when(navBackStackEntry?.destination?.route) {
                    Screens.CameraScreen.route -> {
                        bottomBarState.value = true
                    }
                    Screens.CreateQrScreen.route -> {
                        bottomBarState.value = true
                    }
                    else -> {
                        bottomBarState.value = false
                    }
                }
                Scaffold(
                    containerColor = Color.Transparent,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    bottomBar = {

                        BottomNavBar(
                            bottomBarState = bottomBarState,
                            selectedRoute = currentRoute,
                            onHistoryClick = { /* Handle history click */ },
                            onScanClick = {
                                navController.navigate(Screens.CameraScreen.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(Screens.CameraScreen.route) {
                                        saveState = true
                                    }
                                }
                            },
                            onPlusClick = {
                                navController.navigate(Screens.CreateQrScreen.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(Screens.CameraScreen.route) {
                                        saveState = true
                                    }
                                }
                            }
                        )
                    }
                ) {
                    AppNavigation(
                        navController = navController,
                        contentPadding = it
                    )
                }
            }
        }
    }
}