package id.dev.home.presentation.scan_result

import android.graphics.Bitmap
import id.dev.home.presentation.model.QrTypeIdentifier

data class ScanResultScreenState(
    val qrTypes: QrTypeIdentifier? = null,
    val qrTitle: String = "",
    val content: String = "",
    val titleVal: String = "",
    val isFavorite: Boolean = false,
    val image: Bitmap? = null,
)
