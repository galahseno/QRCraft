package id.dev.core.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [QrItemEntity::class],
    version = 3
)
@AutoMigration(from = 2, to = 3)
abstract class HistoryDatabase: RoomDatabase() {
    abstract val historyDao: HistoryDao
}