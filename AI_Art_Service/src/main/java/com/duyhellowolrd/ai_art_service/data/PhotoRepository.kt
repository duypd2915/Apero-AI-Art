package com.duyhellowolrd.ai_art_service.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

data class PhotoItem(val id: Long, val url: String, val bitmap: Bitmap? = null)

class PhotoRepository(private val context: Context) {

    private val contentResolver = context.contentResolver

    private val mimeTypes = arrayOf("image/jpeg")

    /**
     * Returns a flow that emits one page (100 items) each time it's collected.
     */
    fun loadNextPage(emitSize: Int = 100): Flow<List<PhotoItem>> = flow {
        val imageList = mutableListOf<PhotoItem>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        contentResolver.query(
            queryUri, projection, null, null, sortOrder
        )?.use { cursor ->
            val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val pathColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (cursor.moveToNext()) {
                val path = cursor.getString(pathColumnIndex)
                if (!File(path).exists()) continue
                val id = cursor.getLong(idColumnIndex)
                val imageUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )
//                val bitmap = FileUtils.uriToResizedBitmap(context, imageUri, 200, 200)
                val imageData = PhotoItem(
                    id,
                    imageUri.toString(),
//                    bitmap
                )
                imageList.add(imageData)
                if (imageList.size == emitSize) {
                    imageList.take(emitSize / 3).forEach {
                        preloadImage(it.url)
                    }
                    emit(imageList.toList())
                    imageList.clear()
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun preloadImage(url: String) {
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .size(200)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.DISABLED)
            .build()
        imageLoader.enqueue(request)
    }
}
