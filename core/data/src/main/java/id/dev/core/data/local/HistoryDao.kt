package id.dev.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("""
        SELECT * FROM qr_table 
        WHERE qrCreatedFrom = :source 
        ORDER BY createdAt DESC
    """)
    fun getBySourceFlow(source: String): Flow<List<QrItemEntity>>

    @Query("Select * from qr_table where id = :id")
    suspend fun getQrById(id: String): QrItemEntity?

    @Query("UPDATE qr_table SET title = :newTitle WHERE id = :id")
    suspend fun updateTitleById(id: String, newTitle: String)

    @Upsert
    suspend fun addQrItem(item: QrItemEntity): Long

    @Query("DELETE FROM qr_table WHERE id = :id")
    suspend fun deleteQRById(id: String): Int

    @Query("DELETE FROM qr_table")
    suspend fun clearAll()
}