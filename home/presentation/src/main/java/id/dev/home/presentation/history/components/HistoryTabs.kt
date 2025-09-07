package id.dev.home.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.dev.home.presentation.model.ScanHistoryTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryTabs(
    selectedTab: ScanHistoryTab,
    onTabSelected: (ScanHistoryTab) -> Unit,
) {

    PrimaryTabRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        selectedTabIndex = if (selectedTab == ScanHistoryTab.Scanned) 0 else 1,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(if (selectedTab == ScanHistoryTab.Scanned) 0 else 1),
                width = 176.dp,
                height = 2.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
        }
    ) {
        ScanHistoryTab.entries.forEachIndexed { index, destination ->
            Tab(
                selected = selectedTab == destination,
                onClick = {
                    onTabSelected(destination)
                },
                text = {
                    Text(
                        text = destination.name,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                    )
                },
                selectedContentColor = MaterialTheme.colorScheme.onSurface,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}