package id.dev.home.presentation.create_qr_generator

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.core.domain.model.Result
import id.dev.core.presentation.utils.asUiText
import id.dev.home.domain.HistoryRepository
import id.dev.home.domain.QrItem
import id.dev.home.presentation.model.QrTypeIdentifier
import id.dev.home.presentation.model.QrTypes
import id.dev.home.presentation.model.ScanHistoryTab
import id.dev.home.presentation.model.getContent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GenerateQrScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val qrTypeIdentifier: QrTypeIdentifier? = savedStateHandle.get<String>("qrType")?.let {
        QrTypeIdentifier.fromString(it)
    }

    private val _state = MutableStateFlow(
        GenerateQrScreenState(
            qrTypeIdentifier = qrTypeIdentifier ?: QrTypeIdentifier.TEXT,
            textInput = savedStateHandle[ON_TEXT_CHANGED] ?: "",
            urlInput = savedStateHandle[ON_URL_CHANGED] ?: "",
            contactName = savedStateHandle[ON_CONTACT_NAME_CHANGED] ?: "",
            contactEmail = savedStateHandle[ON_CONTACT_EMAIL_CHANGED] ?: "",
            contactPhone = savedStateHandle[ON_CONTACT_PHONE_CHANGED] ?: "",
            phoneNumber = savedStateHandle[ON_PHONE_NUMBER_CHANGED] ?: "",
            latitude = savedStateHandle[ON_LATITUDE_CHANGED] ?: "",
            longitude = savedStateHandle[ON_LONGITUDE_CHANGED] ?: "",
            wifiSSID = savedStateHandle[ON_WIFI_SSID_CHANGED] ?: "",
            wifiPassword = savedStateHandle[ON_WIFI_PASSWORD_CHANGED] ?: "",
            wifiEncryption = savedStateHandle[ON_WIFI_ENCRYPTION_CHANGED] ?: "",
        )
    )
    val state = _state.asStateFlow()

    private val _events = Channel<GenerateQrCodeEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: GenerateQrCodeAction) {
        when (action) {
            is GenerateQrCodeAction.OnTextChanged -> {
                _state.update {
                    it.copy(
                        textInput = action.text
                    )
                }
                savedStateHandle[ON_TEXT_CHANGED] = action.text
            }

            is GenerateQrCodeAction.OnUrlChanged -> {
                _state.update {
                    it.copy(
                        urlInput = action.url
                    )
                }
                savedStateHandle[ON_URL_CHANGED] = action.url
            }

            is GenerateQrCodeAction.OnContactNameChanged -> {
                _state.update {
                    it.copy(
                        contactName = action.name
                    )
                }
                savedStateHandle[ON_CONTACT_NAME_CHANGED] = action.name
            }

            is GenerateQrCodeAction.OnContactEmailChanged -> {
                _state.update {
                    it.copy(
                        contactEmail = action.email
                    )
                }
                savedStateHandle[ON_CONTACT_EMAIL_CHANGED] = action.email
            }

            is GenerateQrCodeAction.OnContactPhoneChanged -> {
                _state.update {
                    it.copy(
                        contactPhone = action.phone
                    )
                }
                savedStateHandle[ON_CONTACT_PHONE_CHANGED] = action.phone
            }

            is GenerateQrCodeAction.OnPhoneNumberChanged -> {
                _state.update {
                    it.copy(
                        phoneNumber = action.phone
                    )
                }
                savedStateHandle[ON_PHONE_NUMBER_CHANGED] = action.phone
            }

            is GenerateQrCodeAction.OnLatitudeChanged -> {
                _state.update {
                    it.copy(
                        latitude = action.lat
                    )
                }
                savedStateHandle[ON_LATITUDE_CHANGED] = action.lat
            }

            is GenerateQrCodeAction.OnLongitudeChanged -> {
                _state.update {
                    it.copy(
                        longitude = action.lng
                    )
                }
                savedStateHandle[ON_LONGITUDE_CHANGED] = action.lng
            }

            is GenerateQrCodeAction.OnWifiSSIDChanged -> {
                _state.update {
                    it.copy(
                        wifiSSID = action.ssid
                    )
                }
                savedStateHandle[ON_WIFI_SSID_CHANGED] = action.ssid
            }

            is GenerateQrCodeAction.OnWifiPasswordChanged -> {
                _state.update {
                    it.copy(
                        wifiPassword = action.password
                    )
                }
                savedStateHandle[ON_WIFI_PASSWORD_CHANGED] = action.password
            }

            is GenerateQrCodeAction.OnWifiEncryptionChanged -> {
                _state.update {
                    it.copy(
                        wifiEncryption = action.encryption
                    )
                }
                savedStateHandle[ON_WIFI_ENCRYPTION_CHANGED] = action.encryption
            }

            is GenerateQrCodeAction.OnGenerateQrIsClicked -> {
                generateQrCode()
            }

            is GenerateQrCodeAction.OnDismissErrorDialog -> {
                _state.update {
                    it.copy(
                        isGenerateError = false,
                        errorMessage = null
                    )
                }
            }

            else -> Unit
        }
    }

    private fun generateQrCode() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }

        val currentState = _state.value
        val qrCodeData = when (currentState.qrTypeIdentifier) {
            QrTypeIdentifier.TEXT -> {
                QrTypes.Text(currentState.textInput)
            }

            QrTypeIdentifier.LINK -> {
                QrTypes.Link(currentState.urlInput)
            }

            QrTypeIdentifier.CONTACT -> {
                QrTypes.Contact(
                    name = currentState.contactName,
                    email = currentState.contactEmail,
                    phone = currentState.contactPhone
                )
            }

            QrTypeIdentifier.PHONE -> {
                QrTypes.Phone(currentState.phoneNumber)
            }

            QrTypeIdentifier.GEO -> {
                val lat = currentState.latitude.toDoubleOrNull() ?: 0.0
                val lng = currentState.longitude.toDoubleOrNull() ?: 0.0
                QrTypes.Geo(lat, lng)
            }

            QrTypeIdentifier.WIFI -> {
                QrTypes.Wifi(
                    ssid = currentState.wifiSSID,
                    password = currentState.wifiPassword,
                    encryptionType = currentState.wifiEncryption
                )
            }
        }

        _state.update {
            it.copy(
                generatedQrCode = qrCodeData.getContent(),
            )
        }

        viewModelScope.launch {
            val result = historyRepository.addQrItem(
                QrItem(
                    title = _state.value.qrTypeIdentifier.name.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    qrType = _state.value.qrTypeIdentifier.name,
                    content = qrCodeData.getContent(),
                    createdAt = System.currentTimeMillis(),
                    qrCreatedFrom = ScanHistoryTab.Generated.name,
                    isFavorite = false
                )
            )

            when (result) {
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isGenerateError = true,
                            errorMessage = result.error.asUiText(),
                            isLoading = false,
                        )
                    }
                }

                is Result.Success -> {
                    _events.send(GenerateQrCodeEvent.GenerateQrCode(qrId = result.data))
                }
            }
        }
    }

    companion object {
        private const val ON_TEXT_CHANGED = "on_text_changed"
        private const val ON_URL_CHANGED = "on_url_changed"
        private const val ON_CONTACT_NAME_CHANGED = "on_contact_name_changed"
        private const val ON_CONTACT_EMAIL_CHANGED = "on_contact_email_changed"
        private const val ON_CONTACT_PHONE_CHANGED = "on_contact_phone_changed"
        private const val ON_PHONE_NUMBER_CHANGED = "on_phone_number_changed"
        private const val ON_LATITUDE_CHANGED = "on_latitude_changed"
        private const val ON_LONGITUDE_CHANGED = "on_longitude_changed"
        private const val ON_WIFI_SSID_CHANGED = "on_wifi_ssid_changed"
        private const val ON_WIFI_PASSWORD_CHANGED = "on_wifi_password_changed"
        private const val ON_WIFI_ENCRYPTION_CHANGED = "on_wifi_encryption_changed"
    }
}