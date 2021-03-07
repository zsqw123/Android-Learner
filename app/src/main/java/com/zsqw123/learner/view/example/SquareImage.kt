package com.zsqw123.learner.view.example

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.zsqw123.learner.view.apply
import com.zsqw123.learner.view.dp
import com.zsqw123.learner.view.getSquareBitmap

class IconImageView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val size = (75..150).random().dp.toInt()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG) apply {
        textSize = 20.dp
        textAlign = Paint.Align.CENTER
        color = Color.BLACK
    }
    private val bitmap = getSquareBitmap(resources, size)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(size, size + 20.dp.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.drawText("Hello", width / 2f, size + 20.dp.toInt() - textPaint.descent(), textPaint)
    }
}