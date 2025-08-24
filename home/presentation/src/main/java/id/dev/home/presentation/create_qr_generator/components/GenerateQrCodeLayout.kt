package id.dev.home.presentation.create_qr_generator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.R
import id.dev.home.presentation.create_qr_generator.GenerateQrCodeAction

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun GenerateQrCodeLayout(
    modifier: Modifier,
    navigateBack: () -> Unit,
    topBarText: @Composable () -> Unit,
    content: @Composable () -> Unit,
    onAction: (GenerateQrCodeAction) -> Unit,
    buttonState: Boolean,
    isLandscapeMode: Boolean
) {
    if (!isLandscapeMode) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { topBarText() },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = stringResource(R.string.navigate_up)
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            content()

                            Button(
                                enabled = buttonState,
                                onClick = {
                                    onAction(GenerateQrCodeAction.OnGenerateQrIsClicked)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                                    .clip(CircleShape),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (buttonState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Text(
                                    text = if (isLandscapeMode) "Generate & Preview" else stringResource(
                                        R.string.generate_qr_code
                                    ),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = if (buttonState) MaterialTheme.colorScheme.onSurface else Color(
                                            0xFF8C99A2
                                        )
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        content()

                        Button(
                            enabled = buttonState,
                            onClick = {
                                onAction(GenerateQrCodeAction.OnGenerateQrIsClicked)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .clip(CircleShape),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (buttonState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text(
                                text = if (isLandscapeMode) "Generate & Preview" else stringResource(
                                    R.string.generate_qr_code
                                ),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = if (buttonState) MaterialTheme.colorScheme.onSurface else Color(
                                        0xFF8C99A2
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}