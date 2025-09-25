package id.dev.home.presentation.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import androidx.core.graphics.createBitmap

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

            // Create content values for the image
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)

                // For Android 10+ (API 29+)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }

            // Insert the image into MediaStore
            val uri = contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return@withContext Result.failure(IOException("Failed to create MediaStore entry"))

            // Write the bitmap to the output stream
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

            // Mark the image as ready (for Android 10+)
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            contentResolver.update(uri, contentValues, null, null)

            Result.success(uri)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveImageFromUrl(
        imageUrl: String,
        fileName: String
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            // This is a simplified example - in real apps, use proper image loading libraries
            // like Coil, Glide, or Picasso for URL to Bitmap conversion
            val bitmap = createSampleBitmap() // Replace with actual URL loading
            saveImageToDownloads(bitmap, fileName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun createSampleBitmap(): Bitmap {
        // Create a sample bitmap for demonstration
        return createBitmap(400, 400).apply {
            eraseColor(androidx.compose.ui.graphics.Color.Blue.toArgb())
        }
    }
}