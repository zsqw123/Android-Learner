package com.zsqw123.learner.other.permission.storage.save

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import com.zsqw123.learner.other.permission.storage.storageContext
import kotlinx.coroutines.*
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.random.Random

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/5 12:59
 */
class ImageSave(
    var inputStream: InputStream,
    var mimeType: String = "image/jpeg",
    var description: String = "",
    var contentValues: ContentValues? = null,
) : Save {
    var suspendingTask: Deferred<Boolean>? = null

    companion object {
        operator fun invoke(file: File) = ImageSave(file.inputStream())
        operator fun invoke(bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 100): ImageSave {
            var cache = File(storageContext.cacheDir, Date().time.toString())
            while (cache.exists())
                cache = File(storageContext.cacheDir, (Date().time + Random.nextInt(1 shl 30)).toString())
            val save = ImageSave(cache.inputStream())
            save.suspendingTask = GlobalScope.async {
                @Suppress("BlockingMethodInNonBlockingContext")
                cache.createNewFile()
                bitmap.compress(format, quality, cache.outputStream())
            }
            return save
        }
    }

    override suspend fun save(name: String, subPath: String): Boolean = withContext(Dispatchers.IO) {
        try {
            if (!(suspendingTask?.await() ?: return@withContext false)) return@withContext false
            Save.commonMediaSave(
                name, Environment.DIRECTORY_PICTURES, subPath, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                inputStream, contentValues ?: ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, name)
                    put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                    put(MediaStore.Images.Media.DESCRIPTION, description)
                })
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
