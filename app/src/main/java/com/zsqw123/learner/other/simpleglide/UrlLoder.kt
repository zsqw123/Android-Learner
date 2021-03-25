package com.zsqw123.learner.other.simpleglide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class UrlLoder(private val context: Context, private val imageUrl: String) {
    fun load(imageInvoke: (Bitmap) -> Unit) {
        Executors.newSingleThreadExecutor().apply {
            execute {
                val connection = URL(imageUrl).openConnection() as HttpURLConnection
                if (connection.responseCode == 200) {
                    val bitmap = BitmapFactory.decodeStream(connection.inputStream)
                    Executors.newSingleThreadExecutor().apply {
                        execute {
                            Cache(context).storeToCache(imageUrl, bitmap)
                        }
                    }.shutdown()
                    SGlide.loadFrom = LoadFrom.NETWORK
                    imageInvoke(bitmap)
                }
            }
        }.shutdown()
    }
}