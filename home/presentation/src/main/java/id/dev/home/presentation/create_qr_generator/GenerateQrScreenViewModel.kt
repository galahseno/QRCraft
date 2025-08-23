package id.dev.home.presentation.create_qr_generator

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.home.presentation.create_qr.QrTypeIdentifier
import id.dev.home.presentation.model.QrTypes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import timber.log.Timber

class GenerateQrScreenViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val qrTypeIdentifier: QrTypeIdentifier? = savedStateHandle.get<String>("qrType")?.let {
        Json.decodeFromString<QrTypeIdentifier>(it)
    }

    private val _state = MutableStateFlow(
        GenerateQrScreenState(
            qrTypeIdentifier = qrTypeIdentifier,
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
            is GenerateQrCodeAction.OnNavigateUpClicked -> {
                viewModelScope.launch {
                    _events.send(GenerateQrCodeEvent.NavigateBack)
                }
            }
        }
    }
    private fun generateQrCode() {
        val currentState = _state.value
        val qrCodeData = when (currentState.qrTypeIdentifier) {
            QrTypeIdentifier.TEXT -> { QrTypes.Text(currentState.textInput) }
            QrTypeIdentifier.LINK -> { QrTypes.Link(currentState.urlInput) }
            QrTypeIdentifier.CONTACT -> {
                QrTypes.Contact(
                    name = currentState.contactName,
                    email = currentState.contactEmail,
                    phone = currentState.contactPhone
                )
            }
            QrTypeIdentifier.PHONE -> { QrTypes.Phone(currentState.phoneNumber) }
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
            null -> return
        }

        try {
            val qrString = Json.encodeToString(QrTypes.serializer(), qrCodeData)
            _state.value = _state.value.copy(
                generatedQrCode = qrString,
                isLoading = false
            )
            viewModelScope.launch {
                _events.send(GenerateQrCodeEvent.GenerateQrCode(data = qrString))
            }
        } catch (e: Exception) {
            Timber.d("Error: ${e.message}")
            _state.value = _state.value.copy(isLoading = false)
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