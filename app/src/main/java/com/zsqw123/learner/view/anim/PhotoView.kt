package com.zsqw123.learner.view.anim

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Interpolator
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import com.zsqw123.learner.view.dp
import com.zsqw123.learner.view.getSquareBitmap

private val imageSize = 300.dp.toInt()

class PhotoView(context: Context, attrs: AttributeSet?) : View(context, attrs), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getSquareBitmap(resources, imageSize)
    private var offsetX = 0f
    private var offsetY = 0f
    private var offsetXOriginal = 0f
    private var offsetYOriginal = 0f
    private var smallScale = 0f
    private var bigScale = 0f
    private var isBig = false
    private var nowScale = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val runnable = MyRunner()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        offsetXOriginal = (width - bitmap.width) / 2f
        offsetYOriginal = (height - bitmap.height) / 2f
        if (bitmap.width / bitmap.height.toFloat() > width / height.toFloat()) {// 图片 宽 > 高
            smallScale = width / bitmap.width.toFloat() // 小的缩放就是把图片宽边顶到边
            bigScale = height / bitmap.height.toFloat() * 1.5f // 大的缩放就是把图片长边顶到边
        } else { // 图片 宽 < 高
            smallScale = height / bitmap.height.toFloat() // 小的缩放就是把图片长边顶到边
            bigScale = width / bitmap.width.toFloat() * 1.5f// 大的缩放就是把图片宽边顶到边
        }
    }

    private val gestureDetector by lazy { GestureDetectorCompat(context, this) }
    private val objectAnimator by lazy {
        ObjectAnimator.ofFloat(this, "nowScale", 0f, 1f).apply {

        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.translate(offsetX * nowScale, offsetY * nowScale)
        val realScale = smallScale + (bigScale - smallScale) * nowScale
        canvas.scale(realScale, realScale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, offsetXOriginal, offsetYOriginal, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent?): Boolean = true // 这里要拦截触摸事件, 否则触摸事件不消费
    override fun onSingleTapUp(e: MotionEvent?): Boolean = false
    override fun onLongPress(e: MotionEvent?) {}
    override fun onShowPress(e: MotionEvent?) {}

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        if (isBig) {
            offsetX -= distanceX
            offsetY -= distanceY
            constraintBitmap()
            invalidate()
        }
        return false
    }

    private fun constraintBitmap() {
        offsetX = minOf(offsetX, (bitmap.width * bigScale - width) / 2)
        offsetX = maxOf(offsetX, -(bitmap.width * bigScale - width) / 2)
        offsetY = minOf(offsetY, (bitmap.height * bigScale - height) / 2)
        offsetY = maxOf(offsetY, -(bitmap.height * bigScale - height) / 2)
    }

    inner class MyRunner : Runnable {
        override fun run() {
            if (scroller.computeScrollOffset()) {
                offsetX = scroller.currX.toFloat()
                offsetY = scroller.currY.toFloat()
                invalidate()
                postOnAnimation(runnable)
            }
        }
    }

    private val scroller = OverScroller(context)
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        if (isBig) {
            scroller.fling(
                offsetX.toInt(), offsetY.toInt(), velocityX.toInt(), velocityY.toInt(),
                (-(bigScale * bitmap.width - width) / 2).toInt(), ((bigScale * bitmap.width - width) / 2).toInt(),
                (-(bigScale * bitmap.height - height) / 2).toInt(), ((bigScale * bitmap.height - height) / 2).toInt(), 128, 128
            )
            postOnAnimation(runnable)
        }
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        performClick()
        return false
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        isBig = !isBig
        if (isBig) {
            offsetX = (e.x - width / 2f) * (1 - bigScale / smallScale)
            offsetY = (e.y - height / 2f) * (1 - bigScale / smallScale)
            constraintBitmap()
            objectAnimator.start()
        } else objectAnimator.reverse()
        invalidate()
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }
}