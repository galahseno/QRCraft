@file:OptIn(ExperimentalMaterial3Api::class)

package id.dev.home.presentation.create_qr_generator

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.dev.core.presentation.R
import id.dev.core.presentation.utils.DeviceConfiguration
import id.dev.core.presentation.utils.ObserveAsEvents
import id.dev.home.presentation.create_qr_generator.component.GenerateQrCodeLayout
import id.dev.home.presentation.create_qr_generator.component.two_pane.QrPreviewPane
import id.dev.home.presentation.model.QrTypeIdentifier
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GenerateQrCodeRoot(
    onNavigateUp: () -> Unit = {},
    onNavigateToPreview: (String) -> Unit = {},
    viewModel: GenerateQrScreenViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val view = LocalView.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is GenerateQrCodeEvent.GenerateQrCode -> {
                if (deviceConfiguration != DeviceConfiguration.MOBILE_LANDSCAPE) {
                    onNavigateToPreview(event.data)
                }
            }
        }
    }

    GenerateQrCodeScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is GenerateQrCodeAction.OnNavigateUpClicked -> onNavigateUp()
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )

    SideEffect {
        val window = (context as? Activity)?.window
        if (!view.isInEditMode && window != null) {
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                true
        }
    }
}

@Composable
internal fun GenerateQrCodeScreen(
    state: GenerateQrScreenState,
    onAction: (GenerateQrCodeAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val isMobileLandscape = deviceConfiguration == DeviceConfiguration.MOBILE_LANDSCAPE

    val topBarTitle by remember {
        mutableIntStateOf(
            if (isMobileLandscape) {
                id.dev.home.presentation.R.string.generate_preview
            } else {
                when (state.qrTypeIdentifier) {
                    QrTypeIdentifier.TEXT -> R.string.text_qr_code
                    QrTypeIdentifier.LINK -> R.string.link_qr_code
                    QrTypeIdentifier.CONTACT -> R.string.contact_qr_code
                    QrTypeIdentifier.PHONE -> R.string.phone_qr_code
                    QrTypeIdentifier.GEO -> R.string.location_qr_code
                    QrTypeIdentifier.WIFI -> R.string.wi_fi_qr_code
                }
            }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets.displayCutout,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(topBarTitle),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(GenerateQrCodeAction.OnNavigateUpClicked)
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_up)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            GenerateQrCodeLayout(
                state = state,
                onAction = onAction,
                modifier = Modifier.weight(1f),
            )

            if (isMobileLandscape) {
                QrPreviewPane(
                    state = state,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}