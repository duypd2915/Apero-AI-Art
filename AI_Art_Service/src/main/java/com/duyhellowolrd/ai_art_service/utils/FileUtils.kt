package com.duyhellowolrd.ai_art_service.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.graphics.scale
import com.duyhellowolrd.ai_art_service.exception.AiArtException
import com.duyhellowolrd.ai_art_service.exception.ErrorReason
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object FileUtils {
    fun checkImageExtension(context: Context, uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType in listOf("image/jpeg", "image/jpg")
    }

    fun getBitmapForPreview(imagePath: String, imageSize: Int = 200): Bitmap? {
        val file = File(imagePath)
        if (!file.exists()) return null

        val originalBitmap = BitmapFactory.decodeFile(imagePath) ?: return null
        // Step 1: Center-crop to square
        val width = originalBitmap.width
        val height = originalBitmap.height
        val newEdge = minOf(width, height)
        val xOffset = (width - newEdge) / 2
        val yOffset = (height - newEdge) / 2
        val croppedBitmap = Bitmap.createBitmap(originalBitmap, xOffset, yOffset, newEdge, newEdge)

        // Step 2: Resize to target size
        val resizedBitmap = croppedBitmap.scale(imageSize, imageSize)

        // Step 3: Compress to JPEG
        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()

        val result = BitmapFactory.decodeStream(ByteArrayInputStream(byteArray))
        return result
    }

    fun uriToResizedBitmap(
        context: Context,
        uri: Uri,
        maxDimension: Int,
        minDimension: Int
    ): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val width = originalBitmap.width
        val height = originalBitmap.height

        // Calculate scaling factor to fit within minDimension and maxDimension
        val scale = when {
            // Case 1: Both dimensions are within bounds, no scaling needed
            width in minDimension..maxDimension && height in minDimension..maxDimension -> 1.0f
            // Case 2: Scale up if both dimensions are below minDimension
            width < minDimension || height < minDimension -> {
                val scaleWidth = minDimension.toFloat() / width
                val scaleHeight = minDimension.toFloat() / height
                maxOf(
                    scaleWidth,
                    scaleHeight
                ) // Use the larger scale to ensure both dimensions are at least minDimension
            }
            // Case 3: Scale down if any dimension exceeds maxDimension
            else -> {
                val scaleWidth = maxDimension.toFloat() / width
                val scaleHeight = maxDimension.toFloat() / height
                minOf(
                    scaleWidth,
                    scaleHeight
                ) // Use the smaller scale to ensure both dimensions are at most maxDimension
            }
        }

        // Calculate new dimensions
        val newWidth = (width * scale).toInt().coerceIn(minDimension, maxDimension)
        val newHeight = (height * scale).toInt().coerceIn(minDimension, maxDimension)

        return originalBitmap.scale(newWidth, newHeight)
    }


    fun saveBitmapToCache(context: Context, bitmap: Bitmap, fileName: String): File {
        val cacheDir = context.cacheDir
        val file = File(cacheDir, fileName)

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
        }

        return file
    }

    suspend fun saveFileToStorage(fileUrl: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(fileUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    return@withContext Result.failure(AiArtException(ErrorReason.UnknownError))
                }

                val inputStream: InputStream = connection.inputStream
                val downloadsDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) downloadsDir.mkdirs()

                val file =
                    File(downloadsDir, "image_apero_ai_art_${System.currentTimeMillis()}.jpg")

                val outputStream = FileOutputStream(file)
                val buffer = ByteArray(4096)
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()
                return@withContext Result.success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext Result.failure(e)
            }
        }
    }
}