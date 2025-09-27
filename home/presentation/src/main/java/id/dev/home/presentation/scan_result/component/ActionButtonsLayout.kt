package id.dev.home.presentation.scan_result.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.R
import id.dev.core.presentation.component.QRCraftActionButton
import id.dev.home.presentation.utils.copyToClipboard
import id.dev.home.presentation.utils.share

@Composable
internal fun ActionButtonsLayout(
    share: String,
    copyToClipboard: String,
    onSaveClicked: () -> Unit = {},
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        QRCraftActionButton(
            onClick = {
                context.share(share)
            },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.share),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(12.dp)
                )
            }
        )
        QRCraftActionButton(
            onClick = {
                context.copyToClipboard(copyToClipboard)
            },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.copy),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(12.dp)
                )
            },
        )
        QRCraftActionButton(
            modifier = Modifier.fillMaxWidth(),
            buttonText = stringResource(R.string.save),
            buttonTextColor = MaterialTheme.colorScheme.onSurface,
            onClick = onSaveClicked,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.SaveAlt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(12.dp)
                )
            },
        )
    }
}