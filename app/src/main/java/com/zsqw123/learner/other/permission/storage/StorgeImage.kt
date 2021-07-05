package com.zsqw123.learner.other.permission.storage

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/4 22:28
 */
suspend fun ContentResolver.getUris(): List<Uri> = withContext(Dispatchers.IO) {

    val projection = arrayOf(MediaStore.Images.Media._ID)
    val list = arrayListOf<Uri>()
    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, "${MediaStore.Images.Media.DATE_MODIFIED} DESC")?.use { cursor ->
        val id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        while (cursor.moveToNext()) {
            list += ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(id))
        }
    }
    return@withContext list
}
