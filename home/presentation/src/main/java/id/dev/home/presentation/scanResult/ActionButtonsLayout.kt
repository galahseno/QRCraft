package id.dev.home.presentation.scanResult

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.R
import id.dev.core.presentation.component.QRCraftButton
import id.dev.home.presentation.utils.copyToClipboard
import id.dev.home.presentation.utils.shareLink

@Composable
internal fun ActionButtonsLayout(
    share: String,
    copyToClipboard: String
) {

    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QRCraftButton(
            buttonText = stringResource(R.string.share),
            buttonTextColor = MaterialTheme.colorScheme.onSurface,
            onClick = {
                shareLink(context, share)
            },
            modifier = Modifier.weight(1f),
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.share),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(12.dp)
                )
            }
        )
        QRCraftButton(
            buttonText = stringResource(R.string.copy),
            buttonTextColor = MaterialTheme.colorScheme.onSurface,
            onClick = {
                context.copyToClipboard(copyToClipboard)
            },
            modifier = Modifier.weight(1f),
            leadingIcon  = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.copy),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(12.dp)
                )
            },
        )
    }
}