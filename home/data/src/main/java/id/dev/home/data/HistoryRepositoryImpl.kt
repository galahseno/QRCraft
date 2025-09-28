package id.dev.home.data

import id.dev.core.data.local.HistoryDao
import id.dev.core.domain.model.DataError
import id.dev.core.domain.model.Result
import id.dev.home.data.utils.toDomain
import id.dev.home.data.utils.toEntity
import id.dev.home.domain.HistoryRepository
import id.dev.home.domain.QrItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoryRepositoryImpl(
    private val historyDao: HistoryDao
) : HistoryRepository {
    override fun observeQrItemsBySource(source: String): Flow<List<QrItem>> {
        return historyDao.getBySourceFlow(source).map { qrLists -> qrLists.map { it.toDomain() } }
    }

    override suspend fun updateFavoriteById(id: String, isFavorite: Boolean) {
        historyDao.favoriteQrResult(id, isFavorite)
    }

    override suspend fun getQrDataById(id: String): QrItem? {
        return historyDao.getQrById(id)?.toDomain()
    }

    override suspend fun updateTitleById(id: String, newTitle: String) {
        historyDao.updateTitleById(id, newTitle)
    }

    override suspend fun deleteQrItem(id: String) {
        historyDao.deleteQRById(id)
    }

    override suspend fun addQrItem(item: QrItem): Result<Long, DataError.Local> {
        return try {
            Result.Success(data = historyDao.addQrItem(item.toEntity()))
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun clearAll() = historyDao.clearAll()
}