package id.dev.core.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [QrItemEntity::class],
    version = 2
)
@AutoMigration(from = 1, to = 2)
abstract class HistoryDatabase: RoomDatabase() {
    abstract val historyDao: HistoryDao
}