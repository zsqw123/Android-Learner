package com.zsqw123.learner.other.simpleglide

import android.content.Context
import android.graphics.Bitmap
import java.util.*

class WeakRefLoader(private val context: Context, private val imageUrl: String) {
    fun load(imageInvoke: (Bitmap) -> Unit) {
        if (weakHashMap.containsKey(imageUrl)) imageInvoke(weakHashMap[imageUrl]!!)
        else LruLoder(context, imageUrl).load(imageInvoke)
    }

    companion object {
        val weakHashMap = WeakHashMap<String, Bitmap>()
    }
}