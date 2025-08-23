package id.dev.home.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.home.presentation.model.QrTypes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraScreenViewModel() : ViewModel() {

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

            is CameraScreenAction.OnScanResult -> handleScanResult(action.result)
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
            delay(500) // simulate loading

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
                    _event.send(CameraScreenEvent.ScanResult(qrTypes))
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