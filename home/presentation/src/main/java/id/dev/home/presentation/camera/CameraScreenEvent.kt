package id.dev.home.presentation.camera

import id.dev.home.presentation.model.QrTypes

sealed interface CameraScreenEvent {
    data class ScanResult(val result: QrTypes) : CameraScreenEvent
}