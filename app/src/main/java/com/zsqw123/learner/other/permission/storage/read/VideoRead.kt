package com.zsqw123.learner.other.permission.storage.read

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.ArrayMap
import com.zsqw123.learner.other.permission.storage.MediaParams

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/5 22:11
 */
class VideoRead(
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
) : MediaRead {
    lateinit var uri: Uri
    override fun readFromCursor(cursor: Cursor, params: Array<String>, paramIndices: IntArray) = apply {
        val othersMap = ArrayMap<String, String>()
        val id = cursor.getColumnIndexOrThrow(MediaParams.ID)
        for (i in paramIndices.indices) when (params[i]) {
            MediaParams.ID -> uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(id))
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

    companion object {
        suspend fun read(
            filter: String? = null, sortBy: String = MediaParams.DATE_MODIFIED, isAscend: Boolean = false, otherParams: Array<String> = arrayOf()
        ): List<VideoRead> = MediaRead.read(defParams + otherParams, filter, sortBy, isAscend)

        suspend fun read(uri: Uri, otherParams: Array<String> = arrayOf()): VideoRead = MediaRead.read(uri, defParams + otherParams)

        private val defParams = arrayOf(
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