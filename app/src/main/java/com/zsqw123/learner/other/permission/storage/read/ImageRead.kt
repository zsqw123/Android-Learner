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
 * Date 2021/7/5 9:47
 */
class ImageRead(
    var uri: Uri,
    var name: String = "",
    var relativePath: String = "${Environment.DIRECTORY_PICTURES}/",
    var mimeType: String = "image/*",
    var size: Int = 0, // 文件尺寸 bytes
    var dateAdded: Int = 0, //seconds
    var dateModified: Int = 0, //seconds
    var width: Int = 0,
    var height: Int = 0,
    var orientation: Int = 0, // 0 90 180 270
    var others: Map<String, String> = ArrayMap()
) {
    companion object {
        /**
         *
         * @param sortBy String:
         * 排序标准, 参见 see, 建议是 MediaParams 中的, 当然也可以是 MediaColumns 中的,
         * 对于Image, 也可以是 Images.Media(即 ImageColumns), 默认按最后修改时间降序排列
         *
         * @param isAscend Boolean: 按照排序依据升序(true) or 降序(false, 默认)
         * @param filter String:
         * 如: "${MediaParams.HEIGHT} >= 100" 筛选高度大于 100 像素的 Media
         * 如果为空, 则不筛选
         *
         * @param paramsArray Array<String>:
         * 传入要获取的参数, 如: arrayOf(MediaParams.DISPLAY_NAME, MediaParams.SIZE),
         * 如果为空, 则只读取 Uri.
         *
         * @return List<ImageRead>
         *
         * @see MediaParams
         * @see android.provider.MediaStore
         * @see android.provider.MediaStore.Images.Media
         */
        suspend fun read(
            sortBy: String = MediaParams.DATE_MODIFIED, isAscend: Boolean = false, filter: String? = null,
            paramsArray: Array<String> = arrayOf()
        ): List<ImageRead> = withContext(Dispatchers.IO) {
            val projection = arrayOf(MediaStore.Images.Media._ID, *defParams, *paramsArray)
            val list = arrayListOf<ImageRead>()
            storageContext.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                filter, null, "$sortBy ${if (isAscend) "ASC" else "DESC"}"
            )?.use { cursor ->
                val id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val indices = IntArray(projection.size) { cursor.getColumnIndexOrThrow(projection[it]) }
                while (cursor.moveToNext())
                    list += ImageRead(uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(id)))
                        .readFromCursor(cursor, projection, indices)
            }
            list
        }

        suspend fun read(uri: Uri, otherParams: Array<String> = arrayOf()): ImageRead = withContext(Dispatchers.IO) {
            val projection = arrayOf(MediaStore.Images.Media._ID, *defParams, *otherParams)
            val read = ImageRead(uri = uri)
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

        private fun ImageRead.readFromCursor(cursor: Cursor, params: Array<String>, paramIndices: IntArray) = apply {
            val othersMap = ArrayMap<String, String>()
            for (i in 1 until paramIndices.size) when (params[i]) {
                MediaParams.DISPLAY_NAME -> name = cursor.getString(paramIndices[i])
                MediaParams.DATE_ADDED -> dateAdded = cursor.getInt(paramIndices[i])
                MediaParams.DATE_MODIFIED -> dateModified = cursor.getInt(paramIndices[i])
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
            MediaParams.HEIGHT,
            MediaParams.MIME_TYPE,
            MediaParams.ORIENTATION,
            MediaParams.RELATIVE_PATH,
            MediaParams.SIZE,
            MediaParams.WIDTH,
        )
    }
}