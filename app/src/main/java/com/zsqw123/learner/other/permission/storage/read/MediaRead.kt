package com.zsqw123.learner.other.permission.storage.read

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.zsqw123.learner.other.permission.storage.MediaParams
import com.zsqw123.learner.other.permission.storage.MediaType.Companion.TYPE_AUDIO
import com.zsqw123.learner.other.permission.storage.MediaType.Companion.TYPE_IMAGE
import com.zsqw123.learner.other.permission.storage.MediaType.Companion.TYPE_VIDEO
import com.zsqw123.learner.other.permission.storage.mediaUris
import com.zsqw123.learner.other.permission.storage.storageContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/6 20:51
 */
interface MediaRead {
    /**
     * 请不要尝试使用这种方式实例化, 这很危险
     * @return MediaRead
     */
    fun instance(): MediaRead
    fun readFromCursor(cursor: Cursor, params: Array<String>, paramIndices: IntArray): MediaRead

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
        suspend inline fun <reified M : MediaRead> read(
            instance: M, params: Array<String>, filter: String? = null,
            sortBy: String = MediaParams.DATE_MODIFIED, isAscend: Boolean = false
        ): List<M> = withContext(Dispatchers.IO) {
            val list = ArrayList<M>()
            val queryUri = when (instance) {
                is ImageRead -> mediaUris[TYPE_IMAGE]
                is VideoRead -> mediaUris[TYPE_VIDEO]
                is AudioRead -> mediaUris[TYPE_AUDIO]
                else -> throw IllegalArgumentException("read type must be MediaType")
            }
            storageContext.contentResolver.query(
                queryUri, params, filter, null, "$sortBy ${if (isAscend) "ASC" else "DESC"}"
            )?.use { cursor ->
                val indices = IntArray(params.size) { cursor.getColumnIndexOrThrow(params[it]) }
                while (cursor.moveToNext())
                    list += instance.instance().readFromCursor(cursor, params, indices) as M
            }
            list
        }

        /**
         * 使用 uri 读取读取单个文件
         */
        suspend inline fun <M : MediaRead> read(instance: M, uri: Uri, params: Array<String>): M = withContext(Dispatchers.IO) {
            storageContext.contentResolver.query(uri, params, null, null, null)?.use { cursor ->
                val indices = IntArray(params.size) { cursor.getColumnIndexOrThrow(params[it]) }
                cursor.moveToFirst()
                instance.readFromCursor(cursor, params, indices)
            }
            instance
        }
    }
}

