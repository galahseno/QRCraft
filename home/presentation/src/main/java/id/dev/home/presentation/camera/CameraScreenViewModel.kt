package id.dev.home.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.core.domain.model.Result
import id.dev.core.presentation.utils.asUiText
import id.dev.home.domain.HistoryRepository
import id.dev.home.domain.QrItem
import id.dev.home.presentation.model.QrTypes
import id.dev.home.presentation.model.ScanHistoryTab
import id.dev.home.presentation.model.getContent
import id.dev.home.presentation.model.getTitle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraScreenViewModel(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CameraScreenState())
    val state = _state.asStateFlow()

    private val _event = Channel<CameraScreenEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: CameraScreenAction) {
        when (action) {
            is CameraScreenAction.SubmitCameraPermissionInfo -> {
                _state.update {
                    it.copy(
                        hasCameraPermission = action.acceptedCameraPermission,
                        showScreenRationale = action.showCameraRationale
                    )
                }
            }

            is CameraScreenAction.OnScanResult -> handleScanResult(qrTypes = action.result)
            is CameraScreenAction.OnDismissErrorDialog -> handleDismissErrorDialog()
        }
    }

    private fun handleDismissErrorDialog() {
        _state.update {
            it.copy(
                isScanError = false,
                errorMessage = null
            )
        }
    }

    private fun handleScanResult(qrTypes: QrTypes?) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            when (qrTypes) {
                is QrTypes.Error, null -> {
                    _state.update {
                        it.copy(
                            isScanError = true,
                            errorMessage = qrTypes?.message
                        )
                    }
                }

                else -> {
                    val result = historyRepository.addQrItem(
                        QrItem(
                            title = qrTypes.getTitle(),
                            qrType = qrTypes.getTitle().uppercase(),
                            content = qrTypes.getContent(),
                            createdAt = System.currentTimeMillis(),
                            qrCreatedFrom = ScanHistoryTab.Scanned.name
                        )
                    )

                    when(result) {
                        is Result.Error -> {
                            _state.update {
                                it.copy(
                                    isScanError = true,
                                    errorMessage = result.error.asUiText()
                                )
                            }
                        }
                        is Result.Success -> {
                            _event.send(CameraScreenEvent.ScanResult(result.data))
                        }
                    }

                }
            }

            _state.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }
}