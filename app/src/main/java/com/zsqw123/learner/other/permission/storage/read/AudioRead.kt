package com.zsqw123.learner.other.permission.storage.read

import android.content.ContentUris
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.ArrayMap
import com.zsqw123.learner.other.permission.storage.storageContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/5 22:25
 */
class AudioRead(
    var uri: Uri,
    var name: String = "",
    var relativePath: String = "${Environment.DIRECTORY_MUSIC}/",
    var mimeType: String = "audio/*",
    var size: Int = 0, // 文件尺寸 bytes
    var dateAdded: Int = 0, // seconds
    var dateModified: Int = 0, // seconds
    var duration: Int = 0, // seconds
    var others: Map<String, String> = ArrayMap()
) {
    companion object {
        /**
         * @see ImageRead
         */
        suspend fun read(
            sortBy: String = MediaParams.DATE_MODIFIED, isAscend: Boolean = false, filter: String? = null,
            paramsArray: Array<String> = arrayOf()
        ): List<AudioRead> = withContext(Dispatchers.IO) {
            val projection = arrayOf(MediaStore.Video.Media._ID, *defParams, *paramsArray)
            val list = arrayListOf<AudioRead>()
            storageContext.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                filter, null, "$sortBy ${if (isAscend) "ASC" else "DESC"}"
            )?.use { cursor ->
                val id = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val others = ArrayMap<String, String>()

                val indices = IntArray(projection.size) { cursor.getColumnIndexOrThrow(projection[it]) }
                while (cursor.moveToNext()) {
                    val read = AudioRead(uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getLong(id)))
                    for (i in 1 until indices.size) when (projection[i]) {
                        MediaParams.DISPLAY_NAME -> read.name = cursor.getString(indices[i])
                        MediaParams.DATE_ADDED -> read.dateAdded = cursor.getInt(indices[i])
                        MediaParams.DATE_MODIFIED -> read.dateModified = cursor.getInt(indices[i])
                        MediaParams.DURATION -> read.duration = cursor.getInt(indices[i])
                        MediaParams.MIME_TYPE -> read.mimeType = cursor.getString(indices[i])
                        MediaParams.RELATIVE_PATH -> read.relativePath = cursor.getString(indices[i])
                        MediaParams.SIZE -> read.size = cursor.getInt(indices[i])
                        else -> others[projection[i]] = cursor.getString(indices[i])
                    }
                    read.others = others
                    list += read
                }
            }
            return@withContext list
        }

        val defParams = arrayOf(
            MediaParams.DATE_ADDED,
            MediaParams.DATE_MODIFIED,
            MediaParams.DISPLAY_NAME,
            MediaParams.DURATION,
            MediaParams.MIME_TYPE,
            MediaParams.RELATIVE_PATH,
            MediaParams.SIZE,
        )
    }
}