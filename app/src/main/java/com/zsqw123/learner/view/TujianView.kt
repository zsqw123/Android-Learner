package com.zsqw123.learner.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

// az 好丑 需要设计师设计贝塞尔曲线 qwq
class TujianView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    // 这里像素尺寸转换一定要在 onSizeChanged 里, 我这里没考虑性能问题, 也为了方便使用, 直接让他 get 的时候去计算了
    private val Int.r: Float // 像素尺寸转换
        get() = minOf(width, height) / 300f * this
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundPath
        get() = Path().apply {
            moveTo(30.r, 88.r)
            quadTo(100.r, (-62).r, 232.r, 52.r)
            quadTo(359.r, 181.r, 230.r, 263.r)
            quadTo((-65).r, 400.r, 30.r, 88.r)
        }
    private val gradient
        get() = LinearGradient(0f, 0f, 300.r, 300.r, Color.parseColor("#51F7FA"), Color.parseColor("#F3A1FA"), Shader.TileMode.CLAMP)
    private val bigCircle
        get() = Path().apply { addCircle(103.r, 216.r, 24.r, Path.Direction.CCW) }
    private val smallCircle
        get() = Path().apply { addCircle(210.r, 177.r, 20.r, Path.Direction.CCW) }

    override fun onDraw(canvas: Canvas) {
        paint.shader = gradient
        canvas.drawPath(backgroundPath, paint)
        paint.shader = null
        paint.color = Color.RED
        canvas.drawPath(bigCircle, paint)
        canvas.drawPath(smallCircle, paint)
    }
}