package id.dev.home.presentation.scanResult

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.home.presentation.model.QrTypes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import timber.log.Timber

class ScanResultScreenViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val qrTypes = savedStateHandle.get<String>(QR_TYPE) ?: ""

    private val _state = MutableStateFlow(
        ScanResultScreenState(
            titleVal = savedStateHandle[TITLE_VAL] ?: ""
        )
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val parsedResult = try {
                if (qrTypes.isNotEmpty()) {
                    Json.decodeFromString<QrTypes>(qrTypes)
                } else {
                    null
                }
            } catch (e: Exception) {
                Timber.tag("ScanResultScreen").e(e, "Failed to parse barcode result")
                null
            }

            parsedResult?.let { qrTypes ->
                _state.update {
                    it.copy(
                        qrTypes = qrTypes,
                        qrTitle = qrTypes::class.java.simpleName,
                        content = when (qrTypes) {
                            is QrTypes.Contact -> "${qrTypes.name}\n${qrTypes.email}\n${qrTypes.phone}"
                            is QrTypes.Error -> ""
                            is QrTypes.Geo -> "${qrTypes.lat}, ${qrTypes.lng}"
                            is QrTypes.Link -> qrTypes.url
                            is QrTypes.Phone -> qrTypes.number
                            is QrTypes.Text -> qrTypes.content
                            is QrTypes.Wifi -> "SSID: ${qrTypes.ssid}\nPassword: ${qrTypes.password}\nEncryption type: ${qrTypes.encryptionType}"
                        },
                    )
                }

            }
        }
    }

    fun onAction(action: ScanResultScreenAction) {
        when (action) {
            else -> Unit
        }
    }

    companion object {
        private const val QR_TYPE = "qrTypes"
        private const val TITLE_VAL = "titleVal"
    }
}