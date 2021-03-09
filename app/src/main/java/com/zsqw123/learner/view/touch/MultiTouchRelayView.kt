package com.zsqw123.learner.view.touch

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

/** 适配多点触控 跟随手指移动的 Image
 *
 * @property lastExtraX Float 上次 X 轴偏移
 * @property lastExtraY Float 上次 Y 轴偏移
 * @property tmpOffSetX Float 临时 X 轴相对偏移
 * @property tmpOffSetY Float 临时 Y 轴相对偏移
 * @property offsetX Float X 轴绝对偏移
 * @property offsetY Float Y 轴绝对偏移
 * @property trackingId Int 当前追踪的触摸点 id
 *              在每一点触摸事件未停止的时候, id 都是固定的
 */
class MultiTouchRelayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getSquareBitmap(resources, imageSize)

    private var lastExtraX = 0f
    private var lastExtraY = 0f
    private var tmpOffSetX = 0f
    private var tmpOffSetY = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private var trackingId = 0

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                trackingId = event.getPointerId(0) // 按下时获取第一位的id
                tmpOffSetX = event.x
                tmpOffSetY = event.y
                lastExtraX = offsetX
                lastExtraY = offsetY
            }
            MotionEvent.ACTION_MOVE -> {
                val index = event.findPointerIndex(trackingId)
                offsetX = event.getX(index) - tmpOffSetX + lastExtraX
                offsetY = event.getY(index) - tmpOffSetY + lastExtraY
                invalidate()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                trackingId = event.getPointerId(event.actionIndex) // 获取副触摸点的 id
                tmpOffSetX = event.getX(event.findPointerIndex(trackingId))
                tmpOffSetY = event.getY(event.findPointerIndex(trackingId))
                lastExtraX = offsetX
                lastExtraY = offsetY
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val newIndex = event.actionIndex
                if (event.getPointerId(newIndex) == trackingId) {
                    // 判断被追踪的失去, 判断被追踪的是否是最后一个, 切换到上一个触摸点 id
                    // pointer 如果是最后一个那么追踪最后一个一定会抛异常
                    trackingId = if (newIndex == event.pointerCount - 1) {
                        event.getPointerId(event.pointerCount - 2)
                    } else event.getPointerId(event.pointerCount - 1)
                    tmpOffSetX = event.getX(event.findPointerIndex(trackingId))
                    tmpOffSetY = event.getY(event.findPointerIndex(trackingId))
                    lastExtraX = offsetX
                    lastExtraY = offsetY
                }
            }
        }
        return true
    }
}