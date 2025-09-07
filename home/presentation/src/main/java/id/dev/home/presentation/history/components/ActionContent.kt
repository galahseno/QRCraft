package id.dev.home.presentation.history.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.R


@Composable
internal fun ActionContent(
    onDeleteClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = null,
                    onClick = { onShareClick() },
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.share),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = stringResource(R.string.share),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Row(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = null,
                    onClick = { onDeleteClick() },
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id.dev.home.presentation.R.drawable.delete),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )

            Text(
                text = stringResource(R.string.delete),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}