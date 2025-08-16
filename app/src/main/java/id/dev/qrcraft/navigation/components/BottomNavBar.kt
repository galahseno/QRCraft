package id.dev.qrcraft.navigation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.R
import id.dev.core.presentation.theme.QRCraftTheme
import id.dev.core.presentation.theme.link
import id.dev.core.presentation.utils.DeviceConfiguration
import id.dev.core.presentation.utils.applyIf
import id.dev.qrcraft.navigation.screens.Screens

@Composable
fun BottomNavBar(
    selectedRoute: String,
    onHistoryClick: () -> Unit,
    onScanClick: () -> Unit,
    onPlusClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val qRScreenActive = selectedRoute.contains(Screens.CreateQrScreen.toString())
    val historyScreenActive = selectedRoute.contains(Screens.HistoryQrScreen.toString())

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val fabSize = when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT, DeviceConfiguration.MOBILE_LANDSCAPE -> 64.dp
        else -> 72.dp
    }

    val fabIconSize = when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT, DeviceConfiguration.MOBILE_LANDSCAPE -> 22.dp
        else -> 32.dp
    }

    val iconSize = when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT, DeviceConfiguration.MOBILE_LANDSCAPE -> 16.dp
        else -> 24.dp
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .sizeIn(maxWidth = 180.dp, maxHeight = 55.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .applyIf(historyScreenActive) {
                            background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                        }
                        .clickable(
                            interactionSource = null,
                            indication = null
                        ) {
                            onHistoryClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.clock_refresh),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize),
                        tint = if (historyScreenActive) link else MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .applyIf(qRScreenActive) {
                            background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                        }
                        .clickable(
                            interactionSource = null,
                            indication = null
                        ) {
                            onPlusClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.plus_circle),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize),
                        tint = if (qRScreenActive) link else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onScanClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onSurface,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
            shape = CircleShape,
            modifier = Modifier
                .size(fabSize)
                .offset(y = 4.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.scan),
                contentDescription = null,
                modifier = Modifier.size(fabIconSize)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3F3F3)
@Composable
private fun BottomNavBarPreview() {
    QRCraftTheme {
        Box(Modifier.fillMaxSize()) {
            BottomNavBar(
                selectedRoute = Screens.CreateQrScreen.toString(),
                modifier = Modifier.align(Alignment.BottomCenter),
                onHistoryClick = {},
                onScanClick = {},
                onPlusClick = {}
            )
        }
    }
}
