package com.zsqw123.learner.other.permission.storage.save

import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.IntDef
import com.zsqw123.learner.other.permission.storage.storageContext
import kotlinx.coroutines.*
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*
import kotlin.random.Random

typealias AudioSave = FileSave
typealias VideoSave = FileSave
typealias DownloadSave = FileSave

/**
 * @property type Int 类型, 详见 TypeInt
 * @property mainPath String 文件夹路径,
 * 如下载文件夹路径是 Dowload, 参见 Environment.STANDARD_DIRECTORIES
 * @property mimeType String 不必要, 系统其实会自己生成
 * @property contentValues 自定义你的 contentValues 参数
 *
 * @see TypeInt
 * @see Environment
 *
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/5 21:08
 */
class FileSave(
    var inputStream: InputStream,
    @TypeInt var type: Int = TYPE_DOWNLOAD,
    var mainPath: String = Environment.DIRECTORY_DOWNLOADS,
    var mimeType: String = "",
    var contentValues: ContentValues? = null,
) : Save {
    var suspendingTask: Deferred<*>? = null

    companion object {
        const val TYPE_VIDEO = 0
        const val TYPE_AUDIO = 1
        const val TYPE_FILE = 2
        const val TYPE_DOWNLOAD = 3
        operator fun invoke(file: File) = FileSave(file.inputStream())
        operator fun invoke(string: String, charset: Charset = Charsets.UTF_8) = invoke(string.toByteArray(charset))
        operator fun invoke(bytes: ByteArray): FileSave {
            var cache = File(storageContext.cacheDir, Date().time.toString())
            while (cache.exists())
                cache = File(storageContext.cacheDir, (Date().time + Random.nextInt(1 shl 30)).toString())
            val save = FileSave(cache.inputStream())
            save.suspendingTask = GlobalScope.async {
                @Suppress("BlockingMethodInNonBlockingContext")
                cache.createNewFile()
                cache.writeBytes(bytes)
            }
            return save
        }

    }

    override suspend fun save(name: String, subPath: String): Boolean = withContext(Dispatchers.IO) {
        try {
            Save.commonMediaSave(
                name, mainPath, subPath, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                inputStream, contentValues ?: ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                })
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @IntDef(TYPE_AUDIO, TYPE_VIDEO, TYPE_FILE, TYPE_DOWNLOAD)
    annotation class TypeInt
}