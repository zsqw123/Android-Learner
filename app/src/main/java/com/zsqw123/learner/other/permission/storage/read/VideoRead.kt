package com.zsqw123.learner.other.permission.storage.read

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.ArrayMap
import com.zsqw123.learner.app
import com.zsqw123.learner.other.permission.storage.storageContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/5 22:11
 */
class VideoRead(
    var uri: Uri,
    var name: String = "",
    var relativePath: String = "${Environment.DIRECTORY_MOVIES}/",
    var mimeType: String = "video/*",
    var size: Int = 0, // 文件尺寸 bytes
    var dateAdded: Int = 0, // seconds
    var dateModified: Int = 0, // seconds
    var duration: Int = 0, // seconds
    var width: Int = 0,
    var height: Int = 0,
    var orientation: Int = 0, // 0 90 180 270
    var others: Map<String, String> = ArrayMap()
) {
    companion object {
        /**
         * @see ImageRead
         */
        suspend fun read(
            sortBy: String = MediaParams.DATE_MODIFIED, isAscend: Boolean = false, filter: String? = null,
            otherParams: Array<String> = arrayOf()
        ): List<VideoRead> = withContext(Dispatchers.IO) {
            val projection = arrayOf(MediaStore.Video.Media._ID, *defParams, *otherParams)
            val list = arrayListOf<VideoRead>()
            storageContext.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                filter, null, "$sortBy ${if (isAscend) "ASC" else "DESC"}"
            )?.use { cursor ->
                val id = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val indices = IntArray(projection.size) { cursor.getColumnIndexOrThrow(projection[it]) }
                while (cursor.moveToNext())
                    list += VideoRead(uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, cursor.getLong(id)))
                        .readFromCursor(cursor, projection, indices)
            }
            list
        }

        suspend fun read(uri: Uri, otherParams: Array<String> = arrayOf()): VideoRead = withContext(Dispatchers.IO) {
            val projection = arrayOf(MediaStore.Video.Media._ID, *defParams, *otherParams)
            val read = VideoRead(uri = uri)
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

        private fun VideoRead.readFromCursor(cursor: Cursor, params: Array<String>, paramIndices: IntArray) = apply {
            val othersMap = ArrayMap<String, String>()
            for (i in 1 until paramIndices.size) when (params[i]) {
                MediaParams.DISPLAY_NAME -> name = cursor.getString(paramIndices[i])
                MediaParams.DATE_ADDED -> dateAdded = cursor.getInt(paramIndices[i])
                MediaParams.DATE_MODIFIED -> dateModified = cursor.getInt(paramIndices[i])
                MediaParams.DURATION -> duration = cursor.getInt(paramIndices[i])
                MediaParams.HEIGHT -> height = cursor.getInt(paramIndices[i])
                MediaParams.MIME_TYPE -> mimeType = cursor.getString(paramIndices[i])
                MediaParams.ORIENTATION -> orientation
                MediaParams.RELATIVE_PATH -> relativePath = cursor.getString(paramIndices[i])
                MediaParams.SIZE -> size = cursor.getInt(paramIndices[i])
                MediaParams.WIDTH -> width = cursor.getInt(paramIndices[i])
                else -> othersMap[params[i]] = cursor.getString(paramIndices[i])
            }
            others = othersMap
        }

        val defParams = arrayOf(
            MediaParams.DATE_ADDED,
            MediaParams.DATE_MODIFIED,
            MediaParams.DISPLAY_NAME,
            MediaParams.DURATION,
            MediaParams.HEIGHT,
            MediaParams.MIME_TYPE,
            MediaParams.ORIENTATION,
            MediaParams.RELATIVE_PATH,
            MediaParams.SIZE,
            MediaParams.WIDTH,
        )
    }

}