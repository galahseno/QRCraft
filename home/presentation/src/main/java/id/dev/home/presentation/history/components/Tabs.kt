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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Tabs(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedTab = Destination.entries.indexOfFirst { it.route == currentRoute }

    PrimaryTabRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        selectedTabIndex = if (selectedTab >= 0) selectedTab else 0,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(if (selectedTab >= 0) selectedTab else 0),
                width = 176.dp,
                height = 2.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
        }
    ) {
        Destination.entries.forEachIndexed { index, destination ->
            Tab(
                selected = currentRoute == destination.route,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(Destination.SCANNED.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                text = {
                    Text(
                        text = destination.label,
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