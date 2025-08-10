package id.dev.core.presentation.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.R
import id.dev.core.presentation.theme.QRCraftTheme

@Composable
fun QRCraftButton(
    buttonText: String,
    buttonTextColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable (() -> Unit))? = null
) {
    Button(
        modifier = modifier
            .minimumInteractiveComponentSize(),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = buttonText,
            color = buttonTextColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview
@Composable
private fun QRCraftButtonPreview() {
    QRCraftTheme {
        QRCraftButton(
            buttonText = stringResource(R.string.close_app),
            buttonTextColor = MaterialTheme.colorScheme.error,
            onClick = {},
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.share),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .size(12.dp)
                )
            }
        )
    }
}