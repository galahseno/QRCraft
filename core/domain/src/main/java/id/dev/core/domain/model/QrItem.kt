package id.dev.core.domain.model

data class QrItem(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val qrImage: String // it should be bitmap
)
