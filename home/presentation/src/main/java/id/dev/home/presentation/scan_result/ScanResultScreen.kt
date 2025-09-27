package id.dev.home.presentation.scan_result

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.dev.core.presentation.theme.success
import id.dev.core.presentation.utils.ObserveAsEvents
import id.dev.home.presentation.R
import id.dev.home.presentation.scan_result.component.ScanResultCard
import id.dev.home.presentation.scan_result.component.ScanResultTopBar
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScanResultScreenRoot(
    onNavigateUp: () -> Unit,
    viewModel: ScanResultScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ScanResultEvent.SaveImageError -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.failed_to_save, event.message),
                        duration = SnackbarDuration.Short
                    )

                }
            }

            ScanResultEvent.SaveImageSuccess -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.image_saved_to_downloads),
                        duration = SnackbarDuration.Short
                    )

                }
            }
        }
    }

    ScanResultScreen(
        state = state,
        snackbarHostState = snackbarHostState,
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
    snackbarHostState: SnackbarHostState,
    onAction: (ScanResultScreenAction) -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        modifier = Modifier.fillMaxWidth(0.65f),
                        containerColor = success,
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp,
                                alignment = Alignment.CenterHorizontally
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = data.visuals.message,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        },
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
                            onAction(ScanResultScreenAction.OnFavoriteClicked(!state.isFavorite))
                        }
                    ) {
                        Icon(
                            if (state.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = null,
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