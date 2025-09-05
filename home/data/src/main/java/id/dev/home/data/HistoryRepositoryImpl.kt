package id.dev.home.data

import android.util.Log
import id.dev.core.data.local.HistoryDao
import id.dev.core.data.local.toDomain
import id.dev.core.data.local.toEntity
import id.dev.core.domain.model.QrItem
import id.dev.home.domain.repo.HistoryRepository

class HistoryRepositoryImpl(private val historyDao: HistoryDao) : HistoryRepository {

    override suspend fun getAll(): List<QrItem> =
        historyDao.getAll().map { it.toDomain() }

    override suspend fun addQrItem(item: QrItem) {
        try {
            historyDao.addQrItem(item.toEntity())
        } catch (e: Exception) {
            Log.e("HistoryRepositoryImpl", "addQrItem: $e")
            throw e
        }
    }

    override suspend fun clearAll() = historyDao.clearAll()
}