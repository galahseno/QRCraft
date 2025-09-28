package id.dev.home.presentation.history

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.dev.core.presentation.R
import id.dev.home.presentation.history.components.ActionContent
import id.dev.home.presentation.history.components.HistoryContent
import id.dev.home.presentation.history.components.HistoryTabs
import id.dev.home.presentation.model.ScanHistoryTab
import id.dev.home.presentation.utils.share
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanHistoryScreenRoot(
    onItemClick: (String) -> Unit,
    onTopBarCollapsed: (Boolean) -> Unit,
    viewModel: HistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScanHistoryScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is HistoryScreenAction.OnTopBarCollapsed -> {
                    onTopBarCollapsed(action.isCollapsed)
                }

                is HistoryScreenAction.OnItemClick -> {
                    onItemClick(action.qrId)
                }

                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScanHistoryScreen(
    state: HistoryScreenState,
    onAction: (HistoryScreenAction) -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current

    val tabs = ScanHistoryTab.entries
    val pagerState = rememberPagerState(
        initialPage = state.selectedTab.ordinal,
        pageCount = { tabs.size }
    )

    LaunchedEffect(state.selectedTab) {
        val target = state.selectedTab.ordinal
        if (pagerState.currentPage != target) {
            pagerState.animateScrollToPage(target)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                val newTab = tabs[page]
                onAction(HistoryScreenAction.OnTabSelected(newTab))
            }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(scrollBehavior.state.collapsedFraction) {
        if (scrollBehavior.state.collapsedFraction == 1f) {
            onAction(HistoryScreenAction.OnTopBarCollapsed(false))
        } else if (scrollBehavior.state.collapsedFraction == 0f) {
            onAction(HistoryScreenAction.OnTopBarCollapsed(true))
        }
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
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
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            HistoryTabs(
                selectedTab = state.selectedTab,
                onTabSelected = {
                    onAction(HistoryScreenAction.OnTabSelected(it))
                }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
            ) { page ->
                when (tabs[page]) {
                    ScanHistoryTab.Scanned -> HistoryContent(
                        historyItems = state.scannedHistory,
                        onItemClick = {
                            it.id?.let { qrId ->
                                onAction(HistoryScreenAction.OnItemClick(qrId))
                            }
                        },
                        onLongItemClick = {
                            onAction(HistoryScreenAction.OnItemLongClick(it))
                        },
                        onFavoriteClick = {
                            onAction(HistoryScreenAction.OnFavoriteClick(it))
                        }
                    )

                    ScanHistoryTab.Generated -> HistoryContent(
                        historyItems = state.generatedHistory,
                        onItemClick = {
                            it.id?.let { qrId ->
                                onAction(HistoryScreenAction.OnItemClick(qrId))
                            }
                        },
                        onLongItemClick = {
                            onAction(HistoryScreenAction.OnItemLongClick(it))
                        },
                        onFavoriteClick = {
                            onAction(HistoryScreenAction.OnFavoriteClick(it))
                        }
                    )
                }
            }
        }

        if (state.displayBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier
                    .widthIn(max = 425.dp),
                onDismissRequest = {
                    onAction(HistoryScreenAction.OnModalBottomSheetDismiss)
                },
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            ) {
                ActionContent(
                    onDeleteClick = {
                        onAction(HistoryScreenAction.OnDeleteClicked)
                    },
                    onShareClick = {
                        state.selectedQrItem?.let {
                            context.share(state.selectedQrItem.content)
                        }
                    }
                )
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