package id.dev.home.presentation.scanResult

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.home.presentation.model.BarcodeResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import timber.log.Timber

class ScanResultScreenViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val barcodeResult = savedStateHandle.get<String>(BARCODE_RESULT) ?: ""

    private val _state = MutableStateFlow(ScanResultScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val parsedResult = try {
                if (barcodeResult.isNotEmpty()) {
                    Json.decodeFromString<BarcodeResult>(barcodeResult)
                } else {
                    null
                }
            } catch (e: Exception) {
                Timber.tag("ScanResultScreen").e(e, "Failed to parse barcode result")
                null
            }

            _state.update {
                it.copy(
                    barcodeResult = parsedResult,
                )
            }
        }
    }

    fun onAction(action: ScanResultScreenAction) {
        when (action) {
            else -> Unit
        }
    }

    companion object {
        private const val BARCODE_RESULT = "barcodeResult"
    }
}