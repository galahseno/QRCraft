package id.dev.home.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.dev.core.presentation.R
import id.dev.home.presentation.history.components.Destination
import id.dev.home.presentation.history.components.GeneratedContent
import id.dev.home.presentation.history.components.ScannedContent
import id.dev.home.presentation.history.components.Tabs

@Composable
fun ScanHistoryScreenRoot() {
    ScanHistoryScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScanHistoryScreen() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.scan_history),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
        ) {
            Tabs(navController = navController)

            NavHost(
                navController = navController,
                startDestination = Destination.SCANNED.route,
                modifier = Modifier.weight(1f)
            ) {
                Destination.entries.forEach { destination ->
                    composable(destination.route) {
                        when (destination) {
                            Destination.SCANNED -> ScannedContent(DummyData(id ="", title ="",content=""))
                            Destination.GENERATED -> GeneratedContent()
                        }
                    }
                }
            }
        }
    }
}