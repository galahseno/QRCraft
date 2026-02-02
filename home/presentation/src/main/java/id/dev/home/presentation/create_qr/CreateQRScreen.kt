package id.dev.home.presentation.create_qr

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Shortcut
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.dev.core.presentation.R
import id.dev.core.presentation.utils.DeviceConfiguration
import id.dev.home.presentation.create_qr.component.CreateQRTypeCard
import id.dev.home.presentation.model.QrTypeIdentifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateQRRoot(
    onNavigateToGenerator: (QrTypeIdentifier) -> Unit,
    onCreateQrShortcut: () -> Unit,
    viewModel: CreateQRViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CreateQRScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CreateQRAction.SelectQRType -> onNavigateToGenerator(action.qrTypeIdentifier)
                CreateQRAction.CreateQrShortcut -> onCreateQrShortcut()
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQRScreen(
    state: CreateQRState,
    onAction: (CreateQRAction) -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val isWideScreen = deviceConfiguration != DeviceConfiguration.MOBILE_PORTRAIT

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.create_qr),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                ),
                actions = {
                    IconButton(
                        onClick = { onAction(CreateQRAction.CreateQrShortcut) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Shortcut,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = if (isWideScreen) GridCells.Fixed(3) else GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(
                    state.availableTypes.size,
                    key = { it }
                ) { index ->
                    CreateQRTypeCard(
                        createQrTypes = state.availableTypes[index],
                        onClick = {
                            onAction(
                                CreateQRAction.SelectQRType(
                                    state.availableTypes[index].identifier
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    SideEffect {
        val window = (context as? Activity)?.window
        if (!view.isInEditMode && window != null) {
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                true
        }
    }
}