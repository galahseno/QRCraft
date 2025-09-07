package id.dev.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qr_table")
data class QrItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val qrType: String,
    val content: String,
    val createdAt: Long,
    val qrCreatedFrom: String
)