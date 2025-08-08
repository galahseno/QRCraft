package id.dev.home.presentation.camera

import id.dev.home.presentation.model.BarcodeResult

sealed interface CameraScreenEvent {
    data class ScanResult(val result: BarcodeResult) : CameraScreenEvent
}