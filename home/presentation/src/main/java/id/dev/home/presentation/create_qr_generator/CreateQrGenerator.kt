package id.dev.home.presentation.create_qr_generator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.dev.core.presentation.R
import id.dev.core.presentation.utils.DeviceConfiguration
import id.dev.core.presentation.utils.ObserveAsEvents
import id.dev.home.presentation.create_qr.QrTypeIdentifier
import id.dev.home.presentation.scanResult.components.ActionButtonsLayout
import id.dev.home.presentation.scanResult.components.QrCodeImageLayout
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQrGeneratorRoot(
    onNavigateUp: () -> Unit = {},
    onNavigateToPreview: (String) -> Unit = {},
    viewModel: GenerateQrScreenViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val isLandscape = deviceConfiguration == DeviceConfiguration.MOBILE_LANDSCAPE

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is GenerateQrCodeEvent.GenerateQrCode -> {
                if (!isLandscape) {
                    onNavigateToPreview(event.data)
                }
            }
            GenerateQrCodeEvent.NavigateBack -> onNavigateUp()
        }
    }

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            CreateQrGeneratorScreen(
                state = state,
                onAction = viewModel::onAction,
                modifier = Modifier,
            )
        }
        DeviceConfiguration.MOBILE_LANDSCAPE -> {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "Generate & Preview",
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onNavigateUp) {
                                Icon(
                                    Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = stringResource(R.string.navigate_up)
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                        .displayCutoutPadding()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {

                    CreateQrGeneratorScreen(
                        state = state,
                        onAction = viewModel::onAction,
                        modifier = Modifier.weight(1f),
                        isLandscapeMode = true
                    )
                    QrPreviewPane(
                        state = state,
                        modifier = Modifier.width(400.dp),
                    )
                }
            }
        }
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QrPreviewPane(
    state: GenerateQrScreenState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(16.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.generatedQrCode != null) {
                state.qrContent?.let {
                    GeneratedQrCodeDisplay(
                        qrCode = it
                    )
                }
            } else {
                when (state.qrTypeIdentifier) {
                    QrTypeIdentifier.TEXT -> EmptyPreview(message = "Empty Text")
                    QrTypeIdentifier.LINK -> { EmptyPreview(message = "Enter URL to preview") }
                    QrTypeIdentifier.CONTACT ->  EmptyPreview(message = "Enter contact details to preview")
                    QrTypeIdentifier.PHONE ->  EmptyPreview(message = "Enter phone to preview")
                    QrTypeIdentifier.GEO ->  EmptyPreview(message = "Enter coordinates to preview")
                    QrTypeIdentifier.WIFI ->  EmptyPreview(message = "Enter WiFi details to preview")
                    null -> { EmptyPreview(message = "Select QR type to preview") }
                }
            }
        }
    }
}

@Composable
private fun GeneratedQrCodeDisplay(
    qrCode: String,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val cardWidth = when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 380.dp
        DeviceConfiguration.TABLET_PORTRAIT, DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> 480.dp

        else -> 380.dp
    }
    LandscapePreviewUI(
        cardWidth,
        qrCode
    )
}

@Composable
private fun LandscapePreviewUI(
    cardWidth: Dp,
    qrCode: String,
) {
    Box(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .width(cardWidth)
                    .padding(top = 80.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(100.dp))

                    Text(
                        text = qrCode,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    ActionButtonsLayout(
                        share = qrCode,
                        copyToClipboard = qrCode,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        QrCodeImageLayout(
            text = qrCode
        )
    }
}

@Composable
private fun EmptyPreview(
    message: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.QrCode,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQrGeneratorScreen(
    modifier : Modifier,
    state: GenerateQrScreenState,
    onAction: (GenerateQrCodeAction) -> Unit,
    textFieldCustomColor: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        cursorColor = MaterialTheme.colorScheme.onSurface
    ),
    isLandscapeMode: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    when (state.qrTypeIdentifier) {

        QrTypeIdentifier.TEXT -> {
            GenerateQrCodeLayout(
                modifier = modifier,
                navigateBack = { onAction(GenerateQrCodeAction.OnNavigateUpClicked) },
                topBarText = {
                    Text(
                        text = stringResource(R.string.text_qr_code),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                content = {
                    TextField(
                        value = state.textInput,
                        onValueChange = { onAction(GenerateQrCodeAction.OnTextChanged(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .clip(RoundedCornerShape(16.dp)),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.enter_text)
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        colors = textFieldCustomColor
                    )
                },
                onAction = onAction,
                buttonState = state.isTextFormValid,
                isLandscapeMode = isLandscapeMode
            )
        }

        QrTypeIdentifier.LINK -> {
            GenerateQrCodeLayout(
                modifier = modifier,
                navigateBack = { onAction(GenerateQrCodeAction.OnNavigateUpClicked) },
                topBarText = {
                    Text(
                        text = stringResource(R.string.link_qr_code),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                content = {
                    TextField(
                        value = state.urlInput,
                        onValueChange = { onAction(GenerateQrCodeAction.OnUrlChanged(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .clip(RoundedCornerShape(16.dp)),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.enter_url)
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Uri,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        colors = textFieldCustomColor
                    )
                },
                onAction = onAction,
                buttonState = state.isUrlFormValid,
                isLandscapeMode = isLandscapeMode
            )
        }

        QrTypeIdentifier.CONTACT -> {
            GenerateQrCodeLayout(
                modifier = modifier,
                navigateBack = { onAction(GenerateQrCodeAction.OnNavigateUpClicked) },
                topBarText = {
                    Text(
                        text = stringResource(R.string.contact_qr_code),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                content = {
                    Column {
                        TextField(
                            value = state.contactName,
                            onValueChange = { onAction(GenerateQrCodeAction.OnContactNameChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.name)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(
                                        FocusDirection.Down
                                    )
                                }
                            ),
                            colors = textFieldCustomColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = state.contactEmail,
                            onValueChange = { onAction(GenerateQrCodeAction.OnContactEmailChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.email)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(
                                        FocusDirection.Down
                                    )
                                }
                            ),
                            colors = textFieldCustomColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = state.contactPhone,
                            onValueChange = { onAction(GenerateQrCodeAction.OnContactPhoneChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.phone_number)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                            colors = textFieldCustomColor
                        )
                    }
                },
                onAction = onAction,
                buttonState = state.isContactFormValid,
                isLandscapeMode = isLandscapeMode
            )
        }

        QrTypeIdentifier.PHONE -> {
            GenerateQrCodeLayout(
                modifier = modifier,
                navigateBack = { onAction(GenerateQrCodeAction.OnNavigateUpClicked) },
                topBarText = {
                    Text(
                        text = stringResource(R.string.phone_qr_code),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                content = {
                    TextField(
                        value = state.phoneNumber,
                        onValueChange = { onAction(GenerateQrCodeAction.OnPhoneNumberChanged(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .clip(RoundedCornerShape(16.dp)),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.phone_number)
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        colors = textFieldCustomColor
                    )
                },
                onAction = onAction,
                buttonState = state.isPhoneNumberFormValid,
                isLandscapeMode = isLandscapeMode
            )
        }

        QrTypeIdentifier.GEO -> {
            GenerateQrCodeLayout(
                modifier = modifier,
                navigateBack = { onAction(GenerateQrCodeAction.OnNavigateUpClicked) },
                topBarText = {
                    Text(
                        text = stringResource(R.string.location_qr_code),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                content = {
                    Column {
                        TextField(
                            value = state.latitude,
                            onValueChange = { onAction(GenerateQrCodeAction.OnLatitudeChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.latitude)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(
                                        FocusDirection.Down
                                    )
                                }
                            ),
                            colors = textFieldCustomColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = state.longitude,
                            onValueChange = { onAction(GenerateQrCodeAction.OnLongitudeChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.longitude)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                            colors = textFieldCustomColor
                        )
                    }
                },
                onAction = onAction,
                buttonState = state.isLocationFormValid,
                isLandscapeMode = isLandscapeMode
            )
        }

        QrTypeIdentifier.WIFI -> {
            GenerateQrCodeLayout(
                modifier = modifier,
                navigateBack = { onAction(GenerateQrCodeAction.OnNavigateUpClicked) },
                topBarText = {
                    Text(
                        text = stringResource(R.string.wi_fi_qr_code),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                content = {
                    Column {
                        TextField(
                            value = state.wifiSSID,
                            onValueChange = { onAction(GenerateQrCodeAction.OnWifiSSIDChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.wi_fi_name)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(
                                        FocusDirection.Down
                                    )
                                }
                            ),
                            colors = textFieldCustomColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = state.wifiPassword,
                            onValueChange = { onAction(GenerateQrCodeAction.OnWifiPasswordChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.password)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                            colors = textFieldCustomColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        WifiEncryptionDropdown(
                            state = state,
                            onAction = onAction
                        )
                    }
                },
                onAction = onAction,
                buttonState = state.isWifiFormValid,
                isLandscapeMode = isLandscapeMode
            )
        }

        null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.error_no_qr_code_type_was_selected)
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun GenerateQrCodeLayout(
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
                                    text = if (isLandscapeMode) "Generate & Preview" else stringResource(R.string.generate_qr_code),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = if (buttonState) MaterialTheme.colorScheme.onSurface else Color(0xFF8C99A2)
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
                                text = if (isLandscapeMode) "Generate & Preview" else stringResource(R.string.generate_qr_code),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = if (buttonState) MaterialTheme.colorScheme.onSurface else Color(0xFF8C99A2)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiEncryptionDropdown(
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