@file:OptIn(FlowPreview::class)

package id.dev.home.presentation.scan_result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.home.domain.HistoryRepository
import id.dev.home.presentation.model.QrTypeIdentifier
import id.dev.home.presentation.utils.MediaStoreImageSaver
import id.dev.home.presentation.utils.generateQrBitmap
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanResultScreenViewModel(
    private val historyRepository: HistoryRepository,
    private val mediaStoreImageSaver: MediaStoreImageSaver,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val qrId = savedStateHandle.get<String>(QR_ID) ?: ""

    private val _state = MutableStateFlow(
        ScanResultScreenState(
            titleVal = savedStateHandle[TITLE_VAL] ?: ""
        )
    )
    val state = _state.asStateFlow()

    private val _events = Channel<ScanResultEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            val qrItem = historyRepository.getQrDataById(qrId)

            qrItem?.let { qr ->
                val bitmap = generateQrBitmap(qr.content)
                _state.update {
                    it.copy(
                        qrTypes = QrTypeIdentifier.fromString(qr.qrType),
                        qrTitle = qr.title,
                        content = qr.content,
                        isFavorite = qr.isFavorite,
                        image = bitmap
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

            is ScanResultScreenAction.OnSaveIsClicked -> saveQrCodeImage()
            is ScanResultScreenAction.OnFavoriteClicked -> setFavoriteQrCode(action.isFavorite)
            else -> Unit
        }
    }

    private fun setFavoriteQrCode(favorite: Boolean) {
        viewModelScope.launch {
            historyRepository.updateFavoriteById(qrId, favorite)
            _state.update {
                it.copy(isFavorite = favorite)
            }
        }
    }

    private fun saveQrCodeImage() {
        val currentState = _state.value
        val bitmap = currentState.image

        if (bitmap == null) {
            _events.trySend(
                ScanResultEvent.SaveImageError("QR code image not ready")
            )
            return
        }

        viewModelScope.launch {
            try {
                val fileName =
                    "${currentState.qrTitle.ifEmpty { "qr_code" }}_${System.currentTimeMillis()}.png"

                val result = mediaStoreImageSaver.saveImageToDownloads(
                    bitmap = bitmap,
                    fileName = fileName,
                    mimeType = "image/png"
                )

                result.fold(
                    onSuccess = { uri ->
                        _events.send(ScanResultEvent.SaveImageSuccess)
                    },
                    onFailure = { error ->
                        _events.send(
                            ScanResultEvent.SaveImageError(
                                error.message ?: "Failed to save Image"
                            )
                        )
                    }
                )
            } catch (e: Exception) {
                _events.send(ScanResultEvent.SaveImageError(e.message ?: "Failed to save Image"))
            }
        }
    }

    companion object {
        private const val QR_ID = "qrId"
        private const val TITLE_VAL = "titleVal"
        private const val TITLE_SAVING_TIME = 500L
    }
}