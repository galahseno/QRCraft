package id.dev.home.presentation.create_qr_generator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.R
import id.dev.home.presentation.create_qr_generator.GenerateQrCodeAction
import id.dev.home.presentation.create_qr_generator.GenerateQrScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WifiEncryptionDropdown(
    state: GenerateQrScreenState,
    onAction: (GenerateQrCodeAction) -> Unit
) {
    val encryptionOptions = listOf("WEP", "WPA", "WPA2", "WPA3")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))

    ) {
        TextField(
            value = state.wifiEncryption,
            onValueChange = { },
            readOnly = true,
            placeholder = {
                Text(
                    text = stringResource(R.string.encryption_type)
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            encryptionOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onAction(GenerateQrCodeAction.OnWifiEncryptionChanged(option))
                        expanded = false
                    }
                )
            }
        }
    }
}