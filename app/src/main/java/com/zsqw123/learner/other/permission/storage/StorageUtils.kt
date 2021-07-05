package com.zsqw123.learner.other.permission.storage

import android.app.Application
import android.content.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/4 12:39
 */
suspend fun ContentResolver.getPicUris(): List<Uri> = withContext(Dispatchers.IO) {
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val list = arrayListOf<Uri>()
    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, "${MediaStore.Images.Media.DATE_MODIFIED} DESC")?.use { cursor ->
        val id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        while (cursor.moveToNext()) {
            list += ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getString(id).toLong())
        }
    }
    return@withContext list
}

lateinit var storageContext: Application
fun storageInit(application: Application) {
    storageContext = application
}