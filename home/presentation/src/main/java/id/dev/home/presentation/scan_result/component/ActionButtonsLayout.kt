package id.dev.home.presentation.scan_result.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.dev.core.presentation.R
import id.dev.core.presentation.component.QRCraftActionButton
import id.dev.home.presentation.utils.MediaStoreImageSaver
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
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        QRCraftActionButton(
            buttonText = stringResource(R.string.share),
            buttonTextColor = MaterialTheme.colorScheme.onSurface,
            onClick = {
                context.share(share)
            },
            modifier = Modifier,
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
            buttonText = stringResource(R.string.copy),
            buttonTextColor = MaterialTheme.colorScheme.onSurface,
            onClick = {
                context.copyToClipboard(copyToClipboard)
            },
            modifier = Modifier,
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.copy),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(12.dp)
                )
            },
        )
        QrCraftActionButtonWithText(
            onClick = onSaveClicked
        )
    }
}

@Composable
private fun QrCraftActionButtonWithText(
    onClick: () -> Unit,
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        )
    ) {
        Icon(
            imageVector = Icons.Default.SaveAlt,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(12.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(R.string.save),
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 16.sp,
            ),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}