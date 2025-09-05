package id.dev.home.domain.repo

import id.dev.core.domain.model.QrItem

interface HistoryRepository {
    suspend fun getAll(): List<QrItem>
    suspend fun addQrItem(item: QrItem)
    suspend fun clearAll()
}