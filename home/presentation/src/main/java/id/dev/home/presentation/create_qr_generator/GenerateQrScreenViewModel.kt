package id.dev.home.presentation.create_qr_generator

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import id.dev.home.presentation.create_qr.QrCodeTypes
import id.dev.home.presentation.create_qr.QrTypeIdentifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

class GenerateQrScreenViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Get the QrTypeIdentifier from navigation
    private val qrTypeIdentifier: QrTypeIdentifier? = savedStateHandle.get<String>("qrType")?.let {
        Json.decodeFromString<QrTypeIdentifier>(it)
    }

    private val _state = MutableStateFlow(
        GenerateQrScreenState(qrTypeIdentifier = qrTypeIdentifier)
    )
    val state = _state.asStateFlow()

    fun onAction(action: GenerateQrCodeAction) {
        when (action) {
            is GenerateQrCodeAction.OnTextChanged -> {
                _state.value = _state.value.copy(textInput = action.text)
            }
            is GenerateQrCodeAction.OnUrlChanged -> {
                _state.value = _state.value.copy(urlInput = action.url)
            }
            is GenerateQrCodeAction.OnContactNameChanged -> {
                _state.value = _state.value.copy(contactName = action.name)
            }
            is GenerateQrCodeAction.OnContactEmailChanged -> {
                _state.value = _state.value.copy(contactEmail = action.email)
            }
            is GenerateQrCodeAction.OnContactPhoneChanged -> {
                _state.value = _state.value.copy(contactPhone = action.phone)
            }
            is GenerateQrCodeAction.OnPhoneNumberChanged -> {
                _state.value = _state.value.copy(phoneNumber = action.phone)
            }
            is GenerateQrCodeAction.OnLatitudeChanged -> {
                _state.value = _state.value.copy(latitude = action.lat)
            }
            is GenerateQrCodeAction.OnLongitudeChanged -> {
                _state.value = _state.value.copy(longitude = action.lng)
            }
            is GenerateQrCodeAction.OnWifiSSIDChanged -> {
                _state.value = _state.value.copy(wifiSSID = action.ssid)
            }
            is GenerateQrCodeAction.OnWifiPasswordChanged -> {
                _state.value = _state.value.copy(wifiPassword = action.password)
            }
            is GenerateQrCodeAction.OnWifiEncryptionChanged -> {
                _state.value = _state.value.copy(wifiEncryption = action.encryption)
            }
            is GenerateQrCodeAction.OnGenerateQrIsClicked -> {
                generateQrCode()
            }
            is GenerateQrCodeAction.OnNavigateUpClicked -> {
                // Handle navigation
            }
        }
    }

    private fun generateQrCode() {
        val currentState = _state.value
        val qrCodeData = when (currentState.qrTypeIdentifier) {
            QrTypeIdentifier.TEXT -> QrCodeTypes.Text(currentState.textInput)
            QrTypeIdentifier.LINK -> QrCodeTypes.Link(currentState.urlInput)
            QrTypeIdentifier.CONTACT -> QrCodeTypes.Contact(
                name = currentState.contactName,
                email = currentState.contactEmail,
                phone = currentState.contactPhone
            )
            QrTypeIdentifier.PHONE -> QrCodeTypes.Phone(currentState.phoneNumber)
            QrTypeIdentifier.GEO -> {
                val lat = currentState.latitude.toDoubleOrNull() ?: 0.0
                val lng = currentState.longitude.toDoubleOrNull() ?: 0.0
                QrCodeTypes.Geo(lat, lng)
            }
            QrTypeIdentifier.WIFI -> QrCodeTypes.Wifi(
                ssid = currentState.wifiSSID,
                password = currentState.wifiPassword,
                encryptionType = currentState.wifiEncryption
            )
            null -> return
        }

        // Here you would generate the actual QR code with your QR library
        // For now, just storing the data
        _state.value = _state.value.copy(
            generatedQrCode = "Generated QR for: $qrCodeData",
            isLoading = false
        )
    }
}
