package id.dev.home.presentation.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class MediaStoreImageSaver(private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.Q)
    internal suspend fun saveImageToDownloads(
        bitmap: Bitmap,
        fileName: String,
        mimeType: String = "image/jpeg",
        quality: Int = 90
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            val contentResolver = context.contentResolver

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)

                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }

            val uri = contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return@withContext Result.failure(IOException("Failed to create MediaStore entry"))

            contentResolver.openOutputStream(uri)?.use { outputStream ->
                val format = when (mimeType) {
                    "image/png" -> Bitmap.CompressFormat.PNG
                    "image/webp" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        Bitmap.CompressFormat.WEBP_LOSSLESS
                    } else {
                        @Suppress("DEPRECATION")
                        Bitmap.CompressFormat.WEBP
                    }
                    else -> Bitmap.CompressFormat.JPEG
                }

                bitmap.compress(format, quality, outputStream)
            } ?: return@withContext Result.failure(IOException("Failed to open output stream"))

            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            contentResolver.update(uri, contentValues, null, null)

            Result.success(uri)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}