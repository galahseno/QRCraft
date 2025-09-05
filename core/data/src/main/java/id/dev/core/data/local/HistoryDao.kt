package id.dev.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface HistoryDao {
    @Query("SELECT * FROM qritementity")
    suspend fun getAll(): List<QrItemEntity>

    @Upsert
    suspend fun addQrItem(item: QrItemEntity)

    @Query("DELETE FROM qritementity")
    suspend fun clearAll()
}