package id.dev.core.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = primary,
    surface = surface,
    surfaceContainerHigh = surfaceHigher,
    onSurface = onSurface,
    onSurfaceVariant = onSurfaceAlt,
    onPrimaryContainer = linkBg
)

@Composable
fun QRCraftTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
      colorScheme = LightColorScheme,
      typography = Typography,
      content = content
    )
}