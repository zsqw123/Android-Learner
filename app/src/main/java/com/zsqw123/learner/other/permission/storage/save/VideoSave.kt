package com.zsqw123.learner.other.permission.storage.save

import android.content.ContentValues
import android.os.Environment
import com.zsqw123.learner.other.permission.storage.MediaType
import java.io.InputStream

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/7 0:46
 */
class VideoSave(
    inputStream: InputStream? = null,
    @MediaType type: Int = MediaType.TYPE_VIDEO,
    mainPath: String = Environment.DIRECTORY_MOVIES,
    mimeType: String = "video/*",
    contentValues: ContentValues? = null,
) : FileSave(inputStream, type, mainPath, mimeType, contentValues)