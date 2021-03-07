package com.zsqw123.learner.view.example

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zsqw123.learner.view.apply
import com.zsqw123.learner.view.dp

class XferView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val rectSize = 150.dp
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val cicleBitmap =
        Bitmap.createBitmap(300.dp.toInt(), 300.dp.toInt(), Bitmap.Config.ARGB_8888)
    private val rectBitmap =
        Bitmap.createBitmap(300.dp.toInt(), 300.dp.toInt(), Bitmap.Config.ARGB_8888)
    private lateinit var bounds: RectF
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

    init {
        paint.color = Color.parseColor("#555666")
        Canvas(cicleBitmap).apply {
            drawCircle(150.dp, 150.dp, 100.dp, paint)
        }
        paint.color = Color.RED
        Canvas(rectBitmap).apply {
            drawRect(0f, 150.dp, rectSize, 150.dp + rectSize, paint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        bounds = RectF(0f, 0f, width.toFloat(), height.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        val layer = canvas.saveLayer(bounds, null)
        canvas.drawBitmap(cicleBitmap, width / 2f - 150.dp, height / 2f - 150.dp, paint)
        paint.xfermode = xfermode
        canvas.drawBitmap(rectBitmap, width / 2f - 150.dp, height / 2f - 150.dp, paint)
        paint.xfermode = null
        canvas.restoreToCount(layer)
    }
}