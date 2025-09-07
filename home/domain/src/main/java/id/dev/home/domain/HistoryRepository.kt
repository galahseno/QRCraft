package id.dev.home.domain

import id.dev.core.domain.model.DataError
import id.dev.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun observeQrItemsBySource(source: String): Flow<List<QrItem>>
    suspend fun getQrDataById(id: String): QrItem?
    suspend fun updateTitleById(id: String, newTitle: String)
    suspend fun deleteQrItem(id: String)
    suspend fun addQrItem(item: QrItem): Result<Long, DataError.Local>
    suspend fun clearAll()
}