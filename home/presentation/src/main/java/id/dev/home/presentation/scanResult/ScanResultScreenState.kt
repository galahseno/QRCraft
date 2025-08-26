package id.dev.home.presentation.scanResult

import id.dev.home.presentation.model.QrTypes

data class ScanResultScreenState(
    val qrTypes: QrTypes? = null,
    val qrTitle: String = "",
    val content: String = "",
    val titleVal: String = ""
)
