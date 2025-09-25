package id.dev.home.presentation.scan_result

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.dev.home.presentation.scan_result.component.ScanResultCard
import id.dev.home.presentation.scan_result.component.ScanResultTopBar
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
                else -> Unit
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

    // Handle save status with Toast or Snackbar
    LaunchedEffect(state.saveImageSuccess, state.saveImageError) {
        when {
            state.saveImageSuccess == true -> {
                Toast.makeText(context, "QR code saved successfully!", Toast.LENGTH_SHORT).show()
            }
            state.saveImageError != null -> {
                Toast.makeText(context, "Failed to save: ${state.saveImageError}", Toast.LENGTH_LONG).show()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ScanResultTopBar(
                titleVal = state.titleVal,
                onBackClick = {
                    onAction(ScanResultScreenAction.OnNavigateUpClicked)
                },
                actions = {
                    IconButton(
                        onClick = {
                            TODO()
                        }
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null
                        )
                    }
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
                    onTitleChanged = { title ->
                        onAction(ScanResultScreenAction.OnTitleChanged(title))
                    },
                    content = state.content,
                    onSaveClicked = {
                        onAction(ScanResultScreenAction.OnSaveIsClicked)
                    }
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