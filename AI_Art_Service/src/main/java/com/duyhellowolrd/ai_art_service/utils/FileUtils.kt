package com.duyhellowolrd.ai_art_service.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.graphics.scale
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    fun checkImageExtension(context: Context, uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType in listOf("image/jpeg", "image/jpg")
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

    fun saveBitmapToStorage(bitmap: Bitmap) {

    }
}