package com.duyhellowolrd.ai_art_service.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

data class PhotoItem(val id: Long, val uri: Uri)

class PhotoRepository(private val context: Context) {

    private val pageSize = 100
    private var currentOffset = 0
    private var isLastPage = false

    fun reset() {
        currentOffset = 0
        isLastPage = false
    }

    /**
     * Returns a flow that emits one page (100 items) each time it's collected.
     */
    fun loadNextPage(): Flow<List<PhotoItem>> = flow {
        if (isLastPage) {
            emit(emptyList())
            return@flow
        }

        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon()
            .appendQueryParameter("limit", "$currentOffset, $pageSize")
            .build()
        val items = mutableListOf<PhotoItem>()

        context.contentResolver.query(
            queryUri, projection, null, null, sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                items.add(PhotoItem(id, uri))
            }
        }

        if (items.size < pageSize) isLastPage = true else currentOffset += pageSize

        emit(items)
    }.flowOn(Dispatchers.IO)
}
