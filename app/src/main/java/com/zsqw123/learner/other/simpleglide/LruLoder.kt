package com.zsqw123.learner.other.simpleglide

import android.content.Context
import android.graphics.Bitmap
import androidx.core.util.lruCache

class LruLoder(private val context: Context, private val imageUrl: String) {
    fun load(imageInvoke: (Bitmap) -> Unit) {
        val lruGetted = lru.get(imageUrl)
        if (lruGetted == null) CacheLoder(context, imageUrl).load {
            WeakRefLoader.weakHashMap[imageUrl] = it
            imageInvoke(it)
        } else {
            SGlide.loadFrom = LoadFrom.LRU
            WeakRefLoader.weakHashMap[imageUrl] = lruGetted
            imageInvoke(lruGetted)
        }
    }

    companion object {
        private val totalRemainMem = Runtime.getRuntime().totalMemory() shr 20
        val lru = lruCache<String, Bitmap>(totalRemainMem.toInt(), { _, v ->
            return@lruCache v.byteCount shr 20
        })
    }
}
