package com.zsqw123.learner.other.permission.storage

import androidx.annotation.IntDef

/**
 * 用做储存时的信息
 *
 * @param name 建议: name 应该包含后缀
 * @param subPath 储存的次路径, 如图片类型储存在 "/Pictures/awa" 中, 次路径就是"/awa"
 *
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/4 14:28
 *
 */
class StorageInfo(
    var name: String,
    var subPath: String,
)