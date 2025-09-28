package id.dev.home.data.utils

import id.dev.core.data.local.QrItemEntity
import id.dev.home.domain.QrItem


fun QrItemEntity.toDomain(): QrItem = QrItem(
    id = id.toString(),
    title = title,
    qrType = qrType,
    content = content,
    createdAt = createdAt,
    qrCreatedFrom = qrCreatedFrom,
    isFavorite = isFavorite
)

fun QrItem.toEntity(): QrItemEntity = QrItemEntity(
    title = title,
    qrType = qrType,
    content = content,
    createdAt = createdAt,
    qrCreatedFrom = qrCreatedFrom
)