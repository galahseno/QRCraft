package id.dev.home.presentation.scanResult

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.dev.home.presentation.scanResult.component.ScanResultCard
import id.dev.home.presentation.scanResult.component.ScanResultTopBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScanResultScreenRoot(
    onNavigateUp: () -> Unit,
    viewModel: ScanResultScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScanResultScreen(
        state = state,
        onAction = { action ->
            when (action) {
                ScanResultScreenAction.OnNavigateUpClicked -> onNavigateUp()
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
internal fun ScanResultScreen(
    state: ScanResultScreenState,
    onAction: (ScanResultScreenAction) -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ScanResultTopBar(
                titleVal = state.titleVal,
                onBackClick = {
                    onAction(ScanResultScreenAction.OnNavigateUpClicked)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onSurface)
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {
            state.qrTypes?.let {
                ScanResultCard(
                    qrTypes = it,
                    qrTitle = state.qrTitle,
                    content = state.content,
                )
            }
        }
    }

    SideEffect {
        val window = (context as? Activity)?.window
        if (!view.isInEditMode && window != null) {
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                false
        }
    }
}