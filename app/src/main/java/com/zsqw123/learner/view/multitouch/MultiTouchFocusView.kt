package com.zsqw123.learner.view.multitouch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.zsqw123.learner.view.dp
import com.zsqw123.learner.view.getSquareBitmap

private val imageSize = 300.dp.toInt()

/** 适配多点触控 跟随多跟手指中心移动的 Image
 *
 * @property lastExtraX Float 上次 X 轴偏移
 * @property lastExtraY Float 上次 Y 轴偏移
 * @property tmpOffSetX Float 临时 X 轴相对偏移
 * @property tmpOffSetY Float 临时 Y 轴相对偏移
 * @property offsetX Float X 轴绝对偏移
 * @property offsetY Float Y 轴绝对偏移
 */
class MultiTouchFocusView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getSquareBitmap(resources, imageSize)

    private var lastExtraX = 0f
    private var lastExtraY = 0f
    private var tmpOffSetX = 0f
    private var tmpOffSetY = 0f
    private var offsetX = 0f
    private var offsetY = 0f


    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val isUp = event.actionMasked == MotionEvent.ACTION_POINTER_UP
        var count = event.pointerCount
        var sumX = 0f
        var sumY = 0f
        for (i in 0 until count) {
            if (isUp && event.actionIndex == i) continue
            sumX += event.getX(i)
            sumY += event.getY(i)
        }
        if (isUp) count--
        val centerX = sumX / count
        val centerY = sumY / count
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
                tmpOffSetX = centerX
                tmpOffSetY = centerY
                lastExtraX = offsetX
                lastExtraY = offsetY
            }
            MotionEvent.ACTION_MOVE -> {
                offsetX = centerX - tmpOffSetX + lastExtraX
                offsetY = centerY - tmpOffSetY + lastExtraY
                invalidate()
            }
        }
        return true
    }
}