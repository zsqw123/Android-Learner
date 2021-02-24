package com.zsqw123.learner.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CircleImageView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val bitmapSize = 200.dp
    private val roundBitmap = getSquareBitmap(resources, bitmapSize.toInt())
    private val rectBitmap = getRectBitmap(resources, bitmapSize.toInt())
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var circlebounds: RectF
    private lateinit var rectBounds: RectF
    private val xferMode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        circlebounds = RectF(
            (width - bitmapSize) / 2f, (height - bitmapSize) / 2f,
            (width + bitmapSize) / 2f, (height + bitmapSize) / 2f
        )
        rectBounds = RectF(
            (width / 2f - bitmapSize), (height - bitmapSize) / 2f - 220.dp,
            (width / 2f + bitmapSize), (height + bitmapSize) / 2f - 220.dp
        )
    }

    override fun onDraw(canvas: Canvas) {
        val circleLayer = canvas.saveLayer(circlebounds, null)
        canvas.apply {
            drawOval(circlebounds, paint)
            paint.xfermode = xferMode
            drawBitmap(roundBitmap, (width - bitmapSize) / 2f, (height - bitmapSize) / 2f, paint)
            paint.xfermode = null
            restoreToCount(circleLayer)
        }

        val rectLayer = canvas.saveLayer(rectBounds, null)
        canvas.apply {
            drawRoundRect(rectBounds, 12.dp, 12.dp, paint)
            paint.xfermode = xferMode
            drawBitmap(
                rectBitmap, (width / 2f - bitmapSize), (height - bitmapSize) / 2f - 220.dp, paint
            )
            paint.xfermode = null
            restoreToCount(rectLayer)
        }
    }
}