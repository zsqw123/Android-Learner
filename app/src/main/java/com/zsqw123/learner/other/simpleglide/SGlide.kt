package com.zsqw123.learner.other.simpleglide

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper

// exampleImage: https://cdn.jsdelivr.net/gh/zsqw123/cdn@master/picCDN/20210323225931.png
class SGlide(private val context: Context, private val imageUrl: String = "https://cdn.jsdelivr.net/gh/zsqw123/cdn@master/picCDN/20210323225931.png") {
    fun load(imageInvoke: (Bitmap) -> Unit) {
        WeakRefLoader(context, imageUrl).load {
            Handler(Looper.getMainLooper()).post {
                imageInvoke(it)
            }
        }
    }

    companion object {
        var loadFrom = LoadFrom.NETWORK
    }
}

enum class LoadFrom {
    NETWORK, CACHE, LRU, WEAK
}