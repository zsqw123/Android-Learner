package com.zsqw123.learner.other.permission.storage.save

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.WorkerThread
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
    var inputStream: InputStream? = null,
    var mimeType: String = "image/jpeg",
    var description: String = "",
    var contentValues: ContentValues? = null,
) : MediaSave {
    var suspendFile: Deferred<File>? = null

    companion object {
        operator fun invoke(file: File) = ImageSave(file.inputStream())

        /**
         * 涉及到了新建文件, 强烈建议此方法在子线程中执行, 这里没有强制
         * @see WorkerThread
         */
        operator fun invoke(bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 100): ImageSave {
            var cache = File(storageContext.cacheDir, Date().time.toString())
            while (cache.exists())
                cache = File(storageContext.cacheDir, (Date().time + Random.nextInt(1 shl 30)).toString())
            val save = ImageSave()
            save.suspendFile = GlobalScope.async(Dispatchers.IO) {
                cache.createNewFile()
                bitmap.compress(format, quality, cache.outputStream())
                cache
            }
            return save
        }
    }

    suspend fun save() = save(Date().time.toString() + ".jpg", "")
    override suspend fun save(name: String, subPath: String): Boolean = withContext(Dispatchers.IO) {
        try {
            inputStream = inputStream ?: suspendFile?.await()?.inputStream() ?: return@withContext false
            MediaSave.commonMediaSave(
                name, Environment.DIRECTORY_PICTURES, subPath, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                inputStream!!, contentValues ?: ContentValues().apply {
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
