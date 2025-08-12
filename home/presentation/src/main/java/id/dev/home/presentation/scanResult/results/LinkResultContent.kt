package id.dev.home.presentation.scanResult.results

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.R
import id.dev.home.presentation.model.BarcodeResult
import id.dev.home.presentation.scanResult.ActionButtonsLayout
import id.dev.home.presentation.scanResult.ScanResultScreenAction
import id.dev.home.presentation.scanResult.components.QrCodeImageLayout

@Composable
internal fun LinkResultContent(
    modifier: Modifier = Modifier,
    linkResult: BarcodeResult.Link,
    onAction: (ScanResultScreenAction) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                Text(
                    text = linkResult::class.java.simpleName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = linkResult.url,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        .padding(4.dp)
                        .clickable {
                            onAction(ScanResultScreenAction.OnLinkClicked(linkResult.url))
                        }
                )

                Spacer(modifier = Modifier.height(24.dp))

                ActionButtonsLayout(
                    share = "Link: ${linkResult.url}",
                    copyToClipboard = linkResult.url,
                    onAction = onAction
                )
            }
        }
        QrCodeImageLayout()
    }
}