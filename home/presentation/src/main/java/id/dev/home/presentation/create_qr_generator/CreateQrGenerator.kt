package id.dev.home.presentation.create_qr_generator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.dev.home.presentation.create_qr.QrTypeIdentifier
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateQrGeneratorRoot(
    onNavigateUp: () -> Unit = {},
    viewModel: GenerateQrScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CreateQrGeneratorScreen(
        state = state,
        onAction = { action ->
            when (action) {
                GenerateQrCodeAction.OnNavigateUpClicked -> onNavigateUp()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQrGeneratorScreen(
    state: GenerateQrScreenState,
    onAction: (GenerateQrCodeAction) -> Unit,
) {
    when (state.qrTypeIdentifier) {

        QrTypeIdentifier.TEXT -> {
            GenerateQrCodeLayout(
                navigateBack = { onAction(GenerateQrCodeAction.OnNavigateUpClicked) },
                topBarText = { Text(text = "Text QR Code") },
                content = {
                    TextField(
                        value = state.textInput,
                        onValueChange = { onAction(GenerateQrCodeAction.OnTextChanged(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        placeholder = { Text(text = "Enter text") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                },
                onAction = onAction
            )
        }

        QrTypeIdentifier.LINK -> {
            GenerateQrCodeLayout(
                navigateBack = { onAction(GenerateQrCodeAction.OnNavigateUpClicked) },
                topBarText = { Text(text = "Link QR Code") },
                content = {
                    TextField(
                        value = state.urlInput,
                        onValueChange = { onAction(GenerateQrCodeAction.OnUrlChanged(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        placeholder = { Text(text = "Enter URL") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                },
                onAction = onAction
            )
        }

        QrTypeIdentifier.CONTACT -> {
            GenerateQrCodeLayout(
                navigateBack = { onAction(GenerateQrCodeAction.OnNavigateUpClicked) },
                topBarText = { Text(text = "Contact QR Code") },
                content = {
                    Column {
                        TextField(
                            value = state.contactName,
                            onValueChange = { onAction(GenerateQrCodeAction.OnContactNameChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = { Text(text = "Name") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = state.contactEmail,
                            onValueChange = { onAction(GenerateQrCodeAction.OnContactEmailChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = { Text(text = "Email") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = state.contactPhone,
                            onValueChange = { onAction(GenerateQrCodeAction.OnContactPhoneChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = { Text(text = "Phone") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    }
                },
                onAction = onAction
            )
        }

        QrTypeIdentifier.PHONE -> {
            GenerateQrCodeLayout(
                navigateBack = { onAction(GenerateQrCodeAction.OnNavigateUpClicked) },
                topBarText = { Text(text = "Phone QR Code") },
                content = {
                    TextField(
                        value = state.phoneNumber,
                        onValueChange = { onAction(GenerateQrCodeAction.OnPhoneNumberChanged(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        placeholder = { Text(text = "Phone number") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                },
                onAction = onAction
            )
        }

        QrTypeIdentifier.GEO -> {
            GenerateQrCodeLayout(
                navigateBack = { onAction(GenerateQrCodeAction.OnNavigateUpClicked) },
                topBarText = { Text(text = "Location QR Code") },
                content = {
                    Column {
                        TextField(
                            value = state.latitude,
                            onValueChange = { onAction(GenerateQrCodeAction.OnLatitudeChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = { Text(text = "Latitude") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = state.longitude,
                            onValueChange = { onAction(GenerateQrCodeAction.OnLongitudeChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = { Text(text = "Longitude") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    }
                },
                onAction = onAction
            )
        }

        QrTypeIdentifier.WIFI -> {
            GenerateQrCodeLayout(
                navigateBack = { onAction(GenerateQrCodeAction.OnNavigateUpClicked) },
                topBarText = { Text(text = "WiFi QR Code") },
                content = {
                    Column {
                        TextField(
                            value = state.wifiSSID,
                            onValueChange = { onAction(GenerateQrCodeAction.OnWifiSSIDChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = { Text(text = "WiFi Name (SSID)") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = state.wifiPassword,
                            onValueChange = { onAction(GenerateQrCodeAction.OnWifiPasswordChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = { Text(text = "Password") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    }
                },
                onAction = onAction
            )
        }

        null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: No QR type selected")
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun GenerateQrCodeLayout(
    navigateBack: () -> Unit,
    topBarText: @Composable () -> Unit,
    content: @Composable () -> Unit,
    onAction: (GenerateQrCodeAction) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { topBarText() },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Navigate up"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
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
                        content() // Use the passed content here

                        Button(
                            onClick = { onAction(GenerateQrCodeAction.OnGenerateQrIsClicked) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .clip(CircleShape),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text(
                                text = "Generate QR-Code",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = Color(0xFF8C99A2)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}