package com.zsqw123.learner.view

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue
import android.view.View
import androidx.annotation.DrawableRes
import com.zsqw123.learner.R
import okio.Okio
import okio.source
import java.io.File
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.reflect.KProperty

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

fun <T> withInvalidate(initializer: () -> T) = ViewInvalidateDelegates(initializer)

class ViewInvalidateDelegates<T>(val initializer: () -> T) {
    private var mRealValue: T? = null
    operator fun getValue(thisRef: View, property: KProperty<*>): T {
        if (mRealValue == null) mRealValue = initializer()
        return mRealValue!!
    }

    operator fun setValue(thisRef: View, property: KProperty<*>, value: T) {
        thisRef.invalidate()
        mRealValue = value
    }
}

fun distance(x0: Float, y0: Float, x1: Float, y1: Float): Float {
    return sqrt(((x0 - x1).pow(2) + (y0 - y1).pow(2)))
}

fun <T> List<T>.enlargeShuffled(times: Int): List<T> {
    return List(size * times) { get(it % size) }.shuffled()
}

fun main() {

}