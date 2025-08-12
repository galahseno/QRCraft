package id.dev.home.presentation.scanResult.results

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.dev.home.presentation.model.BarcodeResult
import id.dev.home.presentation.scanResult.ActionButtonsLayout
import id.dev.home.presentation.scanResult.ScanResultScreenAction
import id.dev.home.presentation.scanResult.components.QrCodeImageLayout

@Composable
internal fun ContactResultContent(
    modifier: Modifier = Modifier,
    contactResult: BarcodeResult.Contact,
    onAction: (ScanResultScreenAction) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
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
                    text = contactResult::class.java.simpleName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = contactResult.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
                Text(
                    text = contactResult.email,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
                Text(
                    text = contactResult.phone,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                ActionButtonsLayout(
                    share = "${contactResult::class.java.simpleName}: \n Name → ${contactResult.name} \n Email → ${contactResult.email} \n Phone number → ${contactResult.phone}",
                    copyToClipboard = "Name → ${contactResult.name} \n Email → ${contactResult.email} \n Phone number → ${contactResult.phone}",
                    onAction = onAction
                )
            }
        }
        QrCodeImageLayout()
    }
}