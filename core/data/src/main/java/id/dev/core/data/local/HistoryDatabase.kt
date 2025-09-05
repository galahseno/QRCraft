package id.dev.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.dev.core.domain.model.QrItem

@Database(
    entities = [QrItemEntity::class],
    version = 1
)
abstract class HistoryDatabase: RoomDatabase() {
    abstract val historyDao: HistoryDao
}