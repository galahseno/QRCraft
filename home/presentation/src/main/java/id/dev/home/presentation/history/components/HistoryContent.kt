package id.dev.home.presentation.history.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.R
import id.dev.home.domain.QrItem
import id.dev.home.presentation.create_qr.model.CreateQrTypes
import id.dev.home.presentation.utils.toFormattedDateTime

@Composable
internal fun HistoryContent(
    historyItems: List<QrItem>,
    onItemClick: (QrItem) -> Unit,
    onLongItemClick: (QrItem) -> Unit,
    modifier: Modifier = Modifier
) {
    if (historyItems.isEmpty()) {
        Text(
            text = "No history items found.",
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    } else {
        val haptics = LocalHapticFeedback.current
        val listState = rememberLazyListState()
        val lastItemVisible by remember {
            derivedStateOf {
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == historyItems.size - 1
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            LazyColumn(
                state = listState,
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(16.dp)
            ) {
                items(
                    items = historyItems,
                    key = { it.createdAt }
                ) { item ->
                    val createQrTypes = remember {
                        CreateQrTypes.fromString(item.qrType)
                    }
                    ListItem(
                        headlineContent = {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        },
                        modifier = Modifier
                            .combinedClickable(
                                onClick = {
                                    onItemClick(item)
                                },
                                onLongClick = {
                                    haptics.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.LongPress)
                                    onLongItemClick(item)
                                }
                            )
                            .widthIn(max = 550.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .animateItem(),
                        supportingContent = {
                            Column {
                                Text(
                                    text = item.content,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = item.createdAt.toFormattedDateTime(),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.inverseOnSurface
                                    )
                                )
                            }
                        },
                        leadingContent = {
                            Image(
                                imageVector = ImageVector.vectorResource(
                                    createQrTypes?.icon ?: R.drawable.text_ic
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        ),
                    )
                }
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = !lastItemVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                                    MaterialTheme.colorScheme.surface
                                ),
                            )
                        )
                )
            }
        }

    }
}