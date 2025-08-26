package id.dev.core.presentation.component

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import id.dev.core.presentation.theme.QRCraftTheme

@Composable
fun QRCraftButton(
    enable: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        enabled = enable,
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                color = if (enable) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.inverseOnSurface
            )
        )
    }
}

@Preview
@Composable
private fun QRCraftButtonPreview() {
    QRCraftTheme {
        QRCraftButton(
            enable = true,
            text = "GenerateQR",
            onClick = {}
        )
    }
}