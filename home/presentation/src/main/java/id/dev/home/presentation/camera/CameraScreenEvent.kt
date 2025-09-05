package id.dev.home.presentation.camera

import id.dev.core.domain.model.QrItem
import id.dev.home.presentation.model.QrTypes

sealed interface CameraScreenEvent {
    data class ScanResult(val result: QrTypes) : CameraScreenEvent
    data class AddToDatabase(val qrItem: QrItem) : CameraScreenEvent
}