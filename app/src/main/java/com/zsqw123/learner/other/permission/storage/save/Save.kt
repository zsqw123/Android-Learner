package com.zsqw123.learner.other.permission.storage.save

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.zsqw123.learner.other.permission.storage.storageContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/5 9:39
 */
interface Save {
    suspend fun save(name: String = Date().time.toString() + ".jpg", subPath: String = ""): Boolean

    companion object {
        suspend fun commonMediaSave(
            name: String, mainPath: String, subPath: String, contentUri: Uri,
            inputStream: InputStream, contentValues: ContentValues
        ): Boolean = withContext(Dispatchers.IO) {
            @Suppress("DEPRECATION", "BlockingMethodInNonBlockingContext")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val file = File(Environment.getExternalStorageDirectory().absolutePath + "/$mainPath/$subPath", name)
                if (!file.exists()) {
                    file.parentFile?.mkdirs()
                    file.createNewFile()
                }
                val fos = FileOutputStream(file)
                inputStream.copyTo(fos)
                contentValues.put(MediaStore.MediaColumns.DATA, file.absolutePath)
                val uri = storageContext.contentResolver.insert(contentUri, contentValues)
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                intent.data = uri
                storageContext.sendBroadcast(intent)
            } else {
                contentValues.apply {
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "$mainPath/$subPath")
                }
                val item = storageContext.contentResolver.insert(contentUri, contentValues) ?: return@withContext false
                storageContext.contentResolver.openFileDescriptor(item, "w", null).use { pfd ->
                    if (pfd == null) return@withContext false
                    val out = FileOutputStream(pfd.fileDescriptor)
                    inputStream.copyTo(out)
                }
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                storageContext.contentResolver.update(item, contentValues, null, null)
            }
            return@withContext true
        }
    }
}
