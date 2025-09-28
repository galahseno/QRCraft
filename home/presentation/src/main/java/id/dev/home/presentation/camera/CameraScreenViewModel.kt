package id.dev.home.presentation.camera

import android.net.Uri
import androidx.camera.core.TorchState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.core.domain.model.Result
import id.dev.core.presentation.R
import id.dev.core.presentation.utils.UiText
import id.dev.core.presentation.utils.asUiText
import id.dev.home.domain.HistoryRepository
import id.dev.home.domain.QrItem
import id.dev.home.presentation.model.QrTypes
import id.dev.home.presentation.model.ScanHistoryTab
import id.dev.home.presentation.model.getContent
import id.dev.home.presentation.model.getTitle
import id.dev.home.presentation.utils.QrCodeAnalyzer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class CameraScreenViewModel(
    private val historyRepository: HistoryRepository,
    private val qrCodeAnalyzer: QrCodeAnalyzer
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

            is CameraScreenAction.OnCameraControllerReady -> {
                _state.update {
                    it.copy(cameraController = action.controller)
                }
            }

            is CameraScreenAction.OnTorchStateChanged -> {
                val isOn = action.torchState == TorchState.ON
                _state.update {
                    it.copy(isFlashlightOn = isOn)
                }
            }

            is CameraScreenAction.OnFlashlightAvailabilityChanged -> {
                _state.update {
                    it.copy(hasFlashlight = action.hasFlashlight)
                }
            }

            is CameraScreenAction.OnScanResult -> handleScanResult(qrTypes = action.result)

            is CameraScreenAction.OnDismissErrorDialog -> handleDismissErrorDialog()

            is CameraScreenAction.OnFlashlightClicked -> toggleFlashlight()

            is CameraScreenAction.OnImageSelected -> {
                handleImageSelection(action.uri)
            }
        }
    }

    private fun toggleFlashlight() {
        val currentState = _state.value
        if (!currentState.hasFlashlight) return; if (!currentState.hasCameraPermission) return

        val controller = currentState.cameraController
        if (controller == null) return

        try {
            val newTorchState = !currentState.isFlashlightOn
            controller.cameraControl?.enableTorch(newTorchState)
        } catch (e: Exception) {
            Timber.tag("Flashlight").e("Failed to toggle flashlight: ${e.message}")
        }
    }

    private fun handleImageSelection(uri: Uri?) {

        if (uri == null) return

        _state.update {
            it.copy(
                selectedImageUri = uri,
                isProcessingImage = true
            )
        }

        viewModelScope.launch {
            try {
                val result = qrCodeAnalyzer.analyzeImage(uri)

                _state.update {
                    it.copy(isProcessingImage = false)
                }

                handleScanResult(result)

            } catch (e: Exception) {
                Timber.e("Error processing selected image: ${e.message}")
                _state.update {
                    it.copy(
                        isProcessingImage = false,
                        isScanError = true,
                        errorMessage = UiText.StringResource(
                            R.string.image_processing_error,
                            arrayOf(e.message ?: "Unknown error")
                        )
                    )
                }
            }
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
                            qrCreatedFrom = ScanHistoryTab.Scanned.name,
                            isFavorite = false
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