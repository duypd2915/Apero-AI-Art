package com.duyhellowolrd.ai_art_service.data

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import com.duyhellowolrd.ai_art_service.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

data class PhotoItem(val id: Long, val url: String, val bitmap: ImageBitmap)

class PhotoRepository(context: Context) {

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
        val items = mutableListOf<PhotoItem>()

        contentResolver.query(
            queryUri, projection, null, null, sortOrder
        )?.use { cursor ->
            val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val pathColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (cursor.moveToNext()) {
                val path = cursor.getString(pathColumnIndex)
                if (File(path).exists()) {
                    continue
                }
                val id = cursor.getLong(idColumnIndex)
                val imageUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )
                val imageData = PhotoItem(
                    id,
                    imageUri.toString(),
                    FileUtils.loadThumbnail(contentResolver, imageUri, 200, 200)
                )
                imageList.add(imageData)
                if (imageList.size == emitSize) {
                    emit(imageList.toList())
                    imageList.clear()
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}
