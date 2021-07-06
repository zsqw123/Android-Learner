package com.zsqw123.learner.other.permission.storage.read

import android.content.ContentUris
import android.database.Cursor
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
            val projection = arrayOf(MediaStore.Audio.Media._ID, *defParams, *paramsArray)
            val list = arrayListOf<AudioRead>()
            storageContext.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                filter, null, "$sortBy ${if (isAscend) "ASC" else "DESC"}"
            )?.use { cursor ->
                val id = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val indices = IntArray(projection.size) { cursor.getColumnIndexOrThrow(projection[it]) }
                while (cursor.moveToNext())
                    list += AudioRead(uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getLong(id)))
                        .readFromCursor(cursor, projection, indices)
            }
            list
        }

        suspend fun read(uri: Uri, otherParams: Array<String> = arrayOf()): AudioRead = withContext(Dispatchers.IO) {
            val projection = arrayOf(MediaStore.Audio.Media._ID, *defParams, *otherParams)
            val read = AudioRead(uri = uri)
            storageContext.contentResolver.query(
                uri, projection,
                null, null, null
            )?.use { cursor ->
                val indices = IntArray(projection.size) { cursor.getColumnIndexOrThrow(projection[it]) }
                cursor.moveToFirst()
                read.readFromCursor(cursor, projection, indices)
            }
            read
        }

        private fun AudioRead.readFromCursor(cursor: Cursor, params: Array<String>, paramIndices: IntArray) = apply {
            val othersMap = ArrayMap<String, String>()
            for (i in 1 until paramIndices.size) when (params[i]) {
                MediaParams.DISPLAY_NAME -> name = cursor.getString(paramIndices[i])
                MediaParams.DATE_ADDED -> dateAdded = cursor.getInt(paramIndices[i])
                MediaParams.DATE_MODIFIED -> dateModified = cursor.getInt(paramIndices[i])
                MediaParams.DURATION -> duration = cursor.getInt(paramIndices[i])
                MediaParams.MIME_TYPE -> mimeType = cursor.getString(paramIndices[i])
                MediaParams.RELATIVE_PATH -> relativePath = cursor.getString(paramIndices[i])
                MediaParams.SIZE -> size = cursor.getInt(paramIndices[i])
                else -> othersMap[params[i]] = cursor.getString(paramIndices[i])
            }
            others = othersMap
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