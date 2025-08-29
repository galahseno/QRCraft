package id.dev.home.presentation.create_qr_generator.component.two_pane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.utils.DeviceConfiguration
import id.dev.home.presentation.component.QrCodeImageLayout
import id.dev.home.presentation.scanResult.component.ActionButtonsLayout

@Composable
internal fun LandscapePreviewUI(
    qrCode: String,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val cardWidth = when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 380.dp
        DeviceConfiguration.TABLET_PORTRAIT, DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> 480.dp

        else -> 380.dp
    }

    Box(
        modifier = modifier.padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .width(cardWidth)
                    .padding(top = 80.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(100.dp))

                    Text(
                        text = qrCode,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    ActionButtonsLayout(
                        share = qrCode,
                        copyToClipboard = qrCode,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        QrCodeImageLayout(
            text = qrCode
        )
    }
}