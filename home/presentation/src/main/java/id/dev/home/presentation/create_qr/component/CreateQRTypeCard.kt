package id.dev.home.presentation.create_qr.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.dev.home.presentation.create_qr.model.QRType

@Composable
internal fun CreateQRTypeCard(
    qrType: QRType,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .fillMaxWidth()
            .height(108.dp),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(qrType.icon),
                contentDescription = null,
                tint = Color.Unspecified,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(qrType.title),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}