package id.dev.home.presentation.history.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.R

@Composable
internal fun GeneratedContent() {
    LazyColumn {
        items(
            items = listOf("A", "B", "C", "D", "E"),
            key = { it }
        ) { item ->
            ListItem(
                headlineContent = {
                    Text(
                        text = "Generated Item $item",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                supportingContent = {
                    Column {
                        Text(
                            text = "Generated content: $item",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            text = "23.01.2025",
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
        }
    }
}