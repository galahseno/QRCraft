package id.dev.home.presentation.scanResult.results

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.dev.core.presentation.R
import id.dev.home.presentation.model.BarcodeResult
import id.dev.home.presentation.scanResult.ActionButtonsLayout
import id.dev.home.presentation.scanResult.ScanResultScreenAction
import id.dev.home.presentation.scanResult.components.QrCodeImageLayout

@Composable
internal fun TextResultContent(
    modifier: Modifier = Modifier,
    textResult: BarcodeResult.Text,
    onAction: (ScanResultScreenAction) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
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
                    text = textResult::class.java.simpleName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = textResult.content,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier
                        .width(480.dp)
                        .padding(bottom = 4.dp)
                        .align(alignment = Alignment.Start),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (expanded) Int.MAX_VALUE else 6
                )
                Text(
                    text = if (!expanded) stringResource(R.string.show_more) else stringResource(R.string.show_less),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = if (!expanded) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFF8C99A2)
                    ),
                    modifier = Modifier.align(Alignment.Start).clickable {
                        expanded = !expanded
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                ActionButtonsLayout(
                    share = textResult.content,
                    copyToClipboard = textResult.content,
                    onAction = onAction
                )
            }
        }
        QrCodeImageLayout()
    }
}