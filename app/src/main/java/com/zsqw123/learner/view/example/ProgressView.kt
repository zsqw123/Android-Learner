package com.zsqw123.learner.view.example

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zsqw123.learner.view.apply
import com.zsqw123.learner.view.dp

class ProgressView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var count = 30f
        set(value) {
            field = value
            invalidate()
        }
    private var progressColor = Color.MAGENTA
        set(value) {
            field = value
            invalidate()
        }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 16.dp
//        strokeJoin = Paint.Join.ROUND // 拐角处加入方式
        strokeCap = Paint.Cap.ROUND // 画笔边缘形状
    }
    private val innerPaint get() =  Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 14.dp
        strokeCap = Paint.Cap.ROUND
        color = progressColor
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 48.dp
        textAlign = Paint.Align.CENTER
        color = Color.RED
    }
    private val textY get() = height / 2f + (textPaint.descent() - textPaint.ascent()) / 2f
    private val arcRectF get() = RectF(width / 2 - 100.dp, height / 2 - 75.dp, width / 2 + 100.dp, height / 2 + 125.dp)
    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            drawArc(arcRectF, 150f, 240f, false, paint)
            drawArc(arcRectF, 150f, count * 2.4f, false, innerPaint)
            drawText("%.1f%%".format(count), width / 2f, textY, textPaint)
        }
    }
}