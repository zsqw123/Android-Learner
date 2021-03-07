package com.zsqw123.learner.view.anim

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import com.zsqw123.learner.view.dp
import com.zsqw123.learner.view.getSquareBitmap

private val imageSize = 300.dp.toInt()

class PhotoView(context: Context, attrs: AttributeSet?) : View(context, attrs), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getSquareBitmap(resources, imageSize)

    private var offsetX = 0f // 图片实时偏移
    private var offsetY = 0f // 图片实时偏移
    private var currentScale = 0f // 当前缩放比例
        set(value) {
            field = value
            invalidate()
        }
    private val runnable = MyRunner()
    private val scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleEnd(detector: ScaleGestureDetector) {}
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val tmpScale = currentScale * detector.scaleFactor
            return if (tmpScale < smallScale || tmpScale > bigScale) {
                false
            } else {
                currentScale = tmpScale
                true
            }
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            offsetX = (detector.focusX - width / 2f) * (1 - bigScale / smallScale)
            offsetY = (detector.focusY - height / 2f) * (1 - bigScale / smallScale)
            return true
        }
    })

    private var smallScale = 0f // 最小缩放比例
    private var bigScale = 0f // 最大缩放比例
    private var offsetXOriginal = 0f // 默认偏移, 将 Bitmap 偏移到中心
    private var offsetYOriginal = 0f // 默认偏移, 将 Bitmap 偏移到中心
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
        currentScale = smallScale
        objectAnimator.setFloatValues(smallScale, bigScale)
    }

    private val gestureDetector by lazy { GestureDetectorCompat(context, this) }
    private val objectAnimator = ObjectAnimator.ofFloat(this, "currentScale", 0f, 0f)

    override fun onDraw(canvas: Canvas) {
        val fraction = (currentScale - smallScale) / (bigScale - smallScale)
        canvas.translate(offsetX * fraction, offsetY * fraction)
        canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, offsetXOriginal, offsetYOriginal, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress) {
            gestureDetector.onTouchEvent(event)
        }
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean = true // 这里要拦截触摸事件, 否则触摸事件不消费
    override fun onSingleTapUp(e: MotionEvent?): Boolean = false
    override fun onLongPress(e: MotionEvent?) {}
    override fun onShowPress(e: MotionEvent?) {}

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        if (currentScale > smallScale) {
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
        if (currentScale > smallScale) {
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
        when {
            currentScale > bigScale * 0.9 -> { // 当前差不多已经最大了
                objectAnimator.setFloatValues(smallScale, bigScale)
                objectAnimator.reverse()
            }
            currentScale > smallScale * 1.1 -> { // 差不多已经最小了
                offsetX = (e.x - width / 2f) * (1 - bigScale / smallScale)
                offsetY = (e.y - height / 2f) * (1 - bigScale / smallScale)
                constraintBitmap()
                objectAnimator.setFloatValues(currentScale, bigScale)
                objectAnimator.start()
            }
            else -> { // 正中间
                objectAnimator.setFloatValues(smallScale, bigScale)
                objectAnimator.start()
            }
        }
        invalidate()
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }
}