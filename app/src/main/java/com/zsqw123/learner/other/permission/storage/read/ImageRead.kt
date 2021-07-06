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
            val projection = arrayOf(MediaStore.Images.Media._ID, *paramsArray)
            val list = arrayListOf<ImageRead>()
            storageContext.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                filter, null, "$sortBy ${if (isAscend) "ASC" else "DESC"}"
            )?.use { cursor ->
                val id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val others = ArrayMap<String, String>()

                val indices = IntArray(projection.size) { cursor.getColumnIndexOrThrow(projection[it]) }
                while (cursor.moveToNext()) {
                    val read = ImageRead(uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(id)))
                    for (i in 1 until indices.size) when (projection[i]) {
                        MediaParams.DISPLAY_NAME -> read.name = cursor.getString(indices[i])
                        MediaParams.DATE_ADDED -> read.dateAdded = cursor.getInt(indices[i])
                        MediaParams.DATE_MODIFIED -> read.dateModified = cursor.getInt(indices[i])
                        MediaParams.HEIGHT -> read.height = cursor.getInt(indices[i])
                        MediaParams.MIME_TYPE -> read.mimeType = cursor.getString(indices[i])
                        MediaParams.ORIENTATION -> read.orientation
                        MediaParams.RELATIVE_PATH -> read.relativePath = cursor.getString(indices[i])
                        MediaParams.SIZE -> read.size = cursor.getInt(indices[i])
                        MediaParams.WIDTH -> read.width = cursor.getInt(indices[i])
                        else -> others[projection[i]] = cursor.getString(indices[i])
                    }
                    read.others = others
                    list += read
                }
            }
            return@withContext list
        }
    }
}