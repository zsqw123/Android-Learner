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

class SingleTouchView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
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
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                tmpOffSetX = event.x
                tmpOffSetY = event.y
                lastExtraX = offsetX
                lastExtraY = offsetY
            }
            MotionEvent.ACTION_MOVE -> {
                offsetX = event.x - tmpOffSetX + lastExtraX
                offsetY = event.y - tmpOffSetY + lastExtraY
                invalidate()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {

            }
        }
        return true
    }
}