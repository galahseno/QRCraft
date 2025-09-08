@file:OptIn(FlowPreview::class)

package id.dev.home.presentation.scan_result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.home.domain.HistoryRepository
import id.dev.home.presentation.model.QrTypeIdentifier
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanResultScreenViewModel(
    private val historyRepository: HistoryRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val qrId = savedStateHandle.get<String>(QR_ID) ?: ""

    private val _state = MutableStateFlow(
        ScanResultScreenState(
            titleVal = savedStateHandle[TITLE_VAL] ?: ""
        )
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val qrItem = historyRepository.getQrDataById(qrId)

            qrItem?.let { qr ->
                _state.update {
                    it.copy(
                        qrTypes = QrTypeIdentifier.fromString(qr.qrType),
                        qrTitle = qr.title,
                        content = qr.content,
                    )
                }
            } ?: _state.update {
                it.copy(
                    qrTypes = QrTypeIdentifier.TEXT,
                    qrTitle = "NO DATA",
                    content = "No Data Qr Found",
                )
            }
        }

        _state
            .map { it.qrTitle }
            .distinctUntilChanged()
            .debounce(TITLE_SAVING_TIME)
            .onEach {
                if (it.isNotEmpty()) {
                    historyRepository.updateTitleById(qrId, it)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ScanResultScreenAction) {
        when (action) {
            is ScanResultScreenAction.OnTitleChanged -> {
                _state.update {
                    it.copy(
                        qrTitle = action.title
                    )
                }
            }

            else -> Unit
        }
    }

    companion object {
        private const val QR_ID = "qrId"
        private const val TITLE_VAL = "titleVal"
        private const val TITLE_SAVING_TIME = 500L
    }
}