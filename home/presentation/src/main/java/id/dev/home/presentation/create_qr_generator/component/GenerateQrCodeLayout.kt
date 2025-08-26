@file:OptIn(ExperimentalMaterial3Api::class)

package id.dev.home.presentation.create_qr_generator.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.R
import id.dev.core.presentation.component.QRCraftButton
import id.dev.core.presentation.component.QRCraftTextField
import id.dev.home.presentation.create_qr_generator.GenerateQrCodeAction
import id.dev.home.presentation.create_qr_generator.GenerateQrScreenState
import id.dev.home.presentation.model.QrTypeIdentifier

@Composable
internal fun GenerateQrCodeLayout(
    state: GenerateQrScreenState,
    onAction: (GenerateQrCodeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = modifier
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QRCraftTextField(
                    value = when (state.qrTypeIdentifier) {
                        QrTypeIdentifier.TEXT -> state.textInput
                        QrTypeIdentifier.LINK -> state.urlInput
                        QrTypeIdentifier.CONTACT -> state.contactName
                        QrTypeIdentifier.PHONE -> state.phoneNumber
                        QrTypeIdentifier.GEO -> state.latitude
                        QrTypeIdentifier.WIFI -> state.wifiSSID
                    },
                    onValueChange = {
                        when (state.qrTypeIdentifier) {
                            QrTypeIdentifier.TEXT -> {
                                onAction(GenerateQrCodeAction.OnTextChanged(it))
                            }

                            QrTypeIdentifier.LINK -> {
                                onAction(GenerateQrCodeAction.OnUrlChanged(it))
                            }

                            QrTypeIdentifier.CONTACT -> {
                                onAction(GenerateQrCodeAction.OnContactNameChanged(it))
                            }

                            QrTypeIdentifier.PHONE -> {
                                if (it.all { ch -> ch.isDigit() || ch == '+' }) {
                                    onAction(GenerateQrCodeAction.OnPhoneNumberChanged(it))
                                }
                            }

                            QrTypeIdentifier.GEO -> {
                                if (it.all { ch -> ch.isDigit() || ch == '-' }) {
                                    onAction(GenerateQrCodeAction.OnLatitudeChanged(it))
                                }
                            }

                            QrTypeIdentifier.WIFI -> {
                                onAction(GenerateQrCodeAction.OnWifiSSIDChanged(it))
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = when (state.qrTypeIdentifier) {
                            QrTypeIdentifier.LINK -> KeyboardType.Uri
                            QrTypeIdentifier.PHONE -> KeyboardType.Phone
                            QrTypeIdentifier.GEO -> KeyboardType.Decimal
                            else -> KeyboardType.Text
                        },
                        imeAction = when (state.qrTypeIdentifier) {
                            QrTypeIdentifier.TEXT,
                            QrTypeIdentifier.LINK,
                            QrTypeIdentifier.PHONE -> ImeAction.Done

                            QrTypeIdentifier.CONTACT,
                            QrTypeIdentifier.GEO,
                            QrTypeIdentifier.WIFI -> ImeAction.Next
                        },
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    placeholder = when (state.qrTypeIdentifier) {
                        QrTypeIdentifier.TEXT -> stringResource(R.string.enter_text)
                        QrTypeIdentifier.LINK -> stringResource(R.string.enter_url)
                        QrTypeIdentifier.CONTACT -> stringResource(R.string.name)
                        QrTypeIdentifier.PHONE -> stringResource(R.string.phone_number)
                        QrTypeIdentifier.GEO -> stringResource(R.string.latitude)
                        QrTypeIdentifier.WIFI -> stringResource(R.string.wi_fi_name)
                    },
                    singleLine = state.qrTypeIdentifier != QrTypeIdentifier.TEXT
                )

                when (state.qrTypeIdentifier) {
                    QrTypeIdentifier.CONTACT -> {
                        QRCraftTextField(
                            value = state.contactEmail,
                            onValueChange = {
                                onAction(GenerateQrCodeAction.OnContactEmailChanged(it))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next,
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                },
                            ),
                            placeholder = stringResource(R.string.email),
                        )

                        QRCraftTextField(
                            value = state.contactPhone,
                            onValueChange = {
                                if (it.all { ch -> ch.isDigit() || ch == '+' }) {
                                    onAction(GenerateQrCodeAction.OnContactPhoneChanged(it))
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                            placeholder = stringResource(R.string.phone_number),
                        )
                    }

                    QrTypeIdentifier.GEO -> {
                        QRCraftTextField(
                            value = state.longitude,
                            onValueChange = {
                                if (it.all { ch -> ch.isDigit() || ch == '-' }) {
                                    onAction(GenerateQrCodeAction.OnLongitudeChanged(it))
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                            placeholder = stringResource(R.string.longitude),
                        )
                    }

                    QrTypeIdentifier.WIFI -> {
                        QRCraftTextField(
                            value = state.wifiPassword,
                            onValueChange = { onAction(GenerateQrCodeAction.OnWifiPasswordChanged(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                            placeholder = stringResource(R.string.password),
                        )

                        WifiEncryptionDropdown(
                            state = state,
                            onAction = onAction
                        )
                    }

                    else -> Unit
                }

                QRCraftButton(
                    enable = when (state.qrTypeIdentifier) {
                        QrTypeIdentifier.TEXT -> state.isTextFormValid
                        QrTypeIdentifier.LINK -> state.isUrlFormValid
                        QrTypeIdentifier.CONTACT -> state.isContactFormValid
                        QrTypeIdentifier.PHONE -> state.isPhoneNumberFormValid
                        QrTypeIdentifier.GEO -> state.isLocationFormValid
                        QrTypeIdentifier.WIFI -> state.isWifiFormValid
                    },
                    onClick = {
                        onAction(GenerateQrCodeAction.OnGenerateQrIsClicked)
                    },
                    text = stringResource(R.string.generate_qr_code),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape),
                )
            }
        }
    }
}