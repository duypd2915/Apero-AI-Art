package com.duyhellowolrd.ai_art_service.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import com.duyhellowolrd.ai_art_service.exception.AiArtException
import com.duyhellowolrd.ai_art_service.exception.ErrorReason
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

//    private val cache = object : LruCache<Long, ImageBitmap>(200) {
//        override fun sizeOf(key: Long, value: ImageBitmap): Int = 1
//    }

    suspend fun loadThumbnail(
        contentResolver: ContentResolver,
        uri: Uri,
        reqWidth: Int,
        reqHeight: Int
    ): ImageBitmap {
//        cache[id]?.let { return it }
        Log.d("FileUtils", "loadThumbnail: ")
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        val bitmap = contentResolver.openInputStream(uri).use {
            BitmapFactory.decodeStream(it, null, options)
        }
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false

        // 3. Decode bitmap thực sự với sample size
        val resizedBitmap = contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        } ?: createBitmap(reqWidth, reqHeight)

        return resizedBitmap.asImageBitmap()
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight &&
                halfWidth / inSampleSize >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
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