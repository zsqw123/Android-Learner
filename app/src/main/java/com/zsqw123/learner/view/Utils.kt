package com.zsqw123.learner.view

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue
import androidx.annotation.DrawableRes
import com.zsqw123.learner.R

val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
    )

fun getBitmap(resources: Resources, size: Int, @DrawableRes id: Int): Bitmap {
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeResource(resources, id, options)
    options.apply {
        inJustDecodeBounds = false
        inDensity = minOf(outWidth, outHeight)
        inTargetDensity = size
    }
    return BitmapFactory.decodeResource(resources, id, options)
}

fun getSquareBitmap(resources: Resources, size: Int) = getBitmap(resources, size, R.drawable.icon)
fun getRectBitmap(resources: Resources, size: Int) = getBitmap(resources, size, R.drawable.awa)
fun getXsBitmap(resources: Resources, size: Int) = getBitmap(resources, size, R.drawable.xparent)

inline infix fun <reified T> T.apply(method: T.() -> Unit): T {
    method()
    return this
}