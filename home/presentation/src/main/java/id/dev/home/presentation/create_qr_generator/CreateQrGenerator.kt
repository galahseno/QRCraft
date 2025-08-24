@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.dev.core.presentation.R
import id.dev.core.presentation.utils.DeviceConfiguration
import id.dev.core.presentation.utils.ObserveAsEvents
import id.dev.home.presentation.create_qr.QrTypeIdentifier
import id.dev.home.presentation.create_qr_generator.components.EmptyPreview
import id.dev.home.presentation.create_qr_generator.components.GenerateQrCodeLayout
import id.dev.home.presentation.create_qr_generator.components.LandscapePreviewUI
import id.dev.home.presentation.create_qr_generator.components.WifiEncryptionDropdown
import org.koin.compose.viewmodel.koinViewModel

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
            CreateQrGeneratorScreen(
                state = state,
                onAction = viewModel::onAction,
                modifier = Modifier,
            )
        }
    }
}

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
