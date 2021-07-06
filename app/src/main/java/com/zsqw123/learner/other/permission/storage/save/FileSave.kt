package com.zsqw123.learner.other.permission.storage.save

import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.zsqw123.learner.other.permission.storage.MediaType
import com.zsqw123.learner.other.permission.storage.storageContext
import kotlinx.coroutines.*
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*
import kotlin.random.Random

/**
 * @property type Int 类型, 详见 TypeInt
 * @property mainPath String 文件夹路径,
 * 如下载文件夹路径是 Dowload, 参见 Environment.STANDARD_DIRECTORIES
 * @property mimeType String 不必要, 系统其实会自己生成
 * @property contentValues 自定义你的 contentValues 参数
 *
 * @see MediaType
 * @see Environment
 *
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/5 21:08
 */
open class FileSave(
    var inputStream: InputStream? = null,
    @MediaType var type: Int = MediaType.TYPE_DOWNLOAD,
    var mainPath: String = Environment.DIRECTORY_DOWNLOADS,
    var mimeType: String = "",
    var contentValues: ContentValues? = null,
) : MediaSave {
    var suspendFile: Deferred<File>? = null

    companion object {
        operator fun invoke(file: File) = FileSave(file.inputStream())

        /**
         * 涉及到了新建文件, 强烈建议此方法在子线程中执行, 这里没有强制
         * @see WorkerThread
         */
        operator fun invoke(string: String, charset: Charset = Charsets.UTF_8) = FileSave(string.toByteArray(charset))

        /**
         * 涉及到了新建文件, 强烈建议此方法在子线程中执行, 这里没有强制
         * @see WorkerThread
         */
        operator fun invoke(bytes: ByteArray): FileSave {
            var cache = File(storageContext.cacheDir, Date().time.toString())
            while (cache.exists())
                cache = File(storageContext.cacheDir, (Date().time + Random.nextInt(1 shl 30)).toString())
            val save = FileSave()
            save.suspendFile = GlobalScope.async(Dispatchers.IO) {
                cache.createNewFile()
                cache.writeBytes(bytes)
                return@async cache
            }
            return save
        }

    }

    override suspend fun save(name: String, subPath: String): Boolean = withContext(Dispatchers.IO) {
        try {
            inputStream = inputStream ?: suspendFile?.await()?.inputStream() ?: return@withContext false
            MediaSave.commonMediaSave(
                name, mainPath, subPath,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Downloads.EXTERNAL_CONTENT_URI else MediaStore.Files.getContentUri("external"),
                inputStream!!, contentValues ?: ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                })
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}