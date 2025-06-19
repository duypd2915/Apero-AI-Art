package com.duyhellowolrd.ai_art_service.data

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.duyhellowolrd.ai_art_service.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

data class PhotoItem(val id: Long, val url: String, val bitmap: Bitmap)

class PhotoRepository(context: Context) {

    private val contentResolver = context.contentResolver

    suspend fun loadWithPaging(page: Int, pageSize: Int): List<PhotoItem> {
        val imageList = mutableListOf<PhotoItem>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val bundle = Bundle().apply {
            putString(ContentResolver.QUERY_ARG_SQL_SELECTION, null)
            putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, null)
            putString(ContentResolver.QUERY_ARG_SORT_COLUMNS, sortOrder)
            putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)
            putInt(ContentResolver.QUERY_ARG_LIMIT, pageSize)
            putInt(ContentResolver.QUERY_ARG_OFFSET, page * pageSize)
        }
        contentResolver.query(
            queryUri, projection, bundle, null
        )?.use { cursor ->
            Log.d("duypd", "loadWithPaging: query succcess ${cursor.count}")
            val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val pathColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (cursor.moveToNext()) {
                val path = cursor.getString(pathColumnIndex)
                if (!File(path).exists()) continue
                val id = cursor.getLong(idColumnIndex)
                val bitmap = withContext(Dispatchers.Default) {
                    FileUtils.getBitmapForPreview(path)
                }
                bitmap?.let {
                    imageList.add(PhotoItem(id, path, bitmap))
                }
            }
        }
        Log.d("duypd", "loadWithPaging: emit ${imageList.size} ")
        return imageList
    }

    suspend fun getImageCount(): Int {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)
        contentResolver.query(uri, projection, null, null, null).use { cursor ->
            return cursor?.count ?: 0
        }
    }
}
