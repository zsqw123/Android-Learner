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
    val action = fun BitmapFactory.Options.() {
        inJustDecodeBounds = false
        inDensity = minOf(outWidth, outHeight)
        inTargetDensity = size
    }
    options apply action // ¿ 人类迷惑行为
    return BitmapFactory.decodeResource(resources, id, options)
}

fun getSquareBitmap(resources: Resources, size: Int) = getBitmap(resources, size, R.drawable.icon)
fun getRectBitmap(resources: Resources, size: Int) = getBitmap(resources, size, R.drawable.awa)
fun getXsBitmap(resources: Resources, size: Int) = getBitmap(resources, size, R.drawable.xparent)

inline infix fun <reified T> T.apply(action: T.() -> Unit): T {
    action()
    return this
}