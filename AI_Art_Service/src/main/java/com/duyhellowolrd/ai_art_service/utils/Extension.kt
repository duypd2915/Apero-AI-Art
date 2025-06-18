package com.duyhellowolrd.ai_art_service.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

fun Bitmap.compress(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 80
): Bitmap {
    val stream = ByteArrayOutputStream()
    this.compress(format, quality, stream)
    val compressedBytes = stream.toByteArray()
    return BitmapFactory.decodeByteArray(compressedBytes, 0, compressedBytes.size)
}
