package com.zsqw123.learner.other.simpleglide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.util.concurrent.Executors

class CacheLoder(private val context: Context, private val imageUrl: String) {
    fun load(imageInvoke: (Bitmap) -> Unit) {
        Cache(context).getCache(imageUrl) {
            if (it == null) UrlLoder(context, imageUrl).load { bitmap ->
                LruLoder.lru.put(imageUrl, bitmap)
                imageInvoke(bitmap)
            } else imageInvoke(it)
        }
    }
}

class Cache(private val context: Context) {
    private fun getCacheFile(url: String) = File(context.cacheDir, "/SGlide/${caculate(url)}")

    private fun caculate(url: String): String {
        var str = url
        arrayOf("http", "\\", "/", ".", " ", ":").forEach {
            str = str.replace(it, "")
        }
        return str
    }

    fun storeToCache(url: String, bitmap: Bitmap, invoke: () -> Unit = {}) {
        val cacheFile = getCacheFile(url)
        if (!cacheFile.exists()) {
            cacheFile.parentFile?.mkdirs()
            cacheFile.createNewFile()
        }
        Executors.newCachedThreadPool().execute {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, cacheFile.outputStream())
            invoke()
        }
    }

    fun getCache(url: String, bitmapInvoke: (Bitmap?) -> Unit) {
        Executors.newCachedThreadPool().execute {
            bitmapInvoke(getBitmapBlocked(url))
        }
    }

    private fun getBitmapBlocked(url: String): Bitmap? {
        val cacheFile = getCacheFile(url)
        return if (!cacheFile.exists()) null
        else BitmapFactory.decodeStream(cacheFile.inputStream())
    }
}