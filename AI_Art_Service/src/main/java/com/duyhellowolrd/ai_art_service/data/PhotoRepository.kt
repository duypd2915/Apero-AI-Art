package com.duyhellowolrd.ai_art_service.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.paging.PagingState

data class PhotoItem(val id: Long, val uri: Uri)

/**
 * Repository to load all photo URIs from device storage using ContentResolver.
 * @param context Application context
 */
class PhotoRepository(private val context: Context) {

    fun getPhotoPagingSource(): PagingSource<Int, PhotoItem> =
        object : PagingSource<Int, PhotoItem>() {
            private var allIds: List<Long>? = null

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoItem> {
                val page = params.key ?: 0
                val pageSize = params.loadSize

                // Lấy toàn bộ ID nếu chưa có
                if (allIds == null) {
                    val ids = mutableListOf<Long>()
                    val projection = arrayOf(MediaStore.Images.Media._ID)
                    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
                    val query = context.contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        null,
                        null,
                        sortOrder
                    )
                    query?.use { cursor ->
                        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                        while (cursor.moveToNext()) {
                            ids.add(cursor.getLong(idColumn))
                        }
                    }
                    allIds = ids
                }

                val ids = allIds ?: emptyList()
                val fromIndex = page * pageSize
                val toIndex = minOf(fromIndex + pageSize, ids.size)
                if (fromIndex >= ids.size) {
                    return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
                }
                val pageIds = ids.subList(fromIndex, toIndex)
                val photoItems = pageIds.map { id ->
                    val contentUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    PhotoItem(id, contentUri)
                }
                val nextKey = if (toIndex < ids.size) page + 1 else null
                val prevKey = if (page > 0) page - 1 else null
                return LoadResult.Page(
                    data = photoItems,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }

            override fun getRefreshKey(state: PagingState<Int, PhotoItem>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    val anchorPage = state.closestPageToPosition(anchorPosition)
                    anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                }
            }
        }
} 