package id.dev.home.presentation.history.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.R
import id.dev.home.presentation.history.DummyData

@Composable
internal fun ScannedContent(
    someDataClass: DummyData
) {
    val haptics = LocalHapticFeedback.current
    var contextMenuNoteId by rememberSaveable { mutableStateOf<String?>(null) }

    LazyColumn {
        items(
            items = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            key = { it }
        ) { item ->
            ListItem(
                headlineContent = {
                    Text(
                        text = "Scanned Item $item",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                modifier = Modifier
                    .combinedClickable(
                        onClick = {
                            // handle navigation
                        },
                        onLongClick = {
                            // show bottom sheet
                            haptics.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.LongPress)
                            contextMenuNoteId = someDataClass.id
                        }
                    )
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                supportingContent = {
                    Column {
                        Text(
                            text = "Scanned content: $item",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            text = "22.02.1012",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.inverseOnSurface
                            )
                        )
                    }
                },
                leadingContent = {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.link_ic),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
            )
            if (contextMenuNoteId != null) {
                ActionSheet(
                    isVisible = someDataClass.id == contextMenuNoteId,
                    onDismissSheet = { contextMenuNoteId = null }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewScannedContent() {
    ScannedContent(
        someDataClass = DummyData(
            id = "1",
            title = "Hello world",
            content = "Lorem bla bla bla"
        )
    )
}