@file:OptIn(ExperimentalMaterial3Api::class)

package id.dev.home.presentation.camera.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import id.dev.core.presentation.component.QRCraftButton
import id.dev.core.presentation.theme.QRCraftTheme
import id.dev.home.presentation.R

@Composable
fun CameraPermissionDialog(
    onCloseClick: () -> Unit,
    onGrantAccessClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = {},
        modifier = modifier
            .defaultMinSize(320.dp)
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(12.dp)
            ),
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        content = {
            Column(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.camera_required),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.camera_dialog_desc),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    QRCraftButton(
                        buttonText = stringResource(id.dev.core.presentation.R.string.close_app),
                        buttonTextColor = MaterialTheme.colorScheme.error,
                        onClick = onCloseClick
                    )
                    QRCraftButton(
                        buttonText = stringResource(id.dev.core.presentation.R.string.grant_access),
                        buttonTextColor = MaterialTheme.colorScheme.onSurface,
                        onClick = onGrantAccessClick
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun CameraPermissionDialogPreview() {
    QRCraftTheme {
        CameraPermissionDialog(
            onCloseClick = {},
            onGrantAccessClick = {}
        )
    }
}