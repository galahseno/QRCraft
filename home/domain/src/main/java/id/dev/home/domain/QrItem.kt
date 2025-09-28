package id.dev.home.domain

data class QrItem(
    val id: String? = null,
    val title: String,
    val qrType: String,
    val content: String,
    val createdAt: Long,
    val qrCreatedFrom: String,
    val isFavorite: Boolean
)