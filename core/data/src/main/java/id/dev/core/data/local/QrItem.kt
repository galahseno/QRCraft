package id.dev.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import id.dev.core.domain.model.QrItem

@Entity
data class QrItemEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val qrImage: String
)

fun QrItemEntity.toDomain(): QrItem = QrItem(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    qrImage = qrImage
)

fun QrItem.toEntity(): QrItemEntity = QrItemEntity(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    qrImage = qrImage
)