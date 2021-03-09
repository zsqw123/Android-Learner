package com.zsqw123.learner.view.multitouch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import androidx.core.util.forEach
import com.zsqw123.learner.view.dp

class PainterView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 4.dp
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        style = Paint.Style.STROKE
    }

    // key 为 pointerId
    private var pathArray = SparseArray<Path>()

    override fun onDraw(canvas: Canvas) {
        pathArray.forEach { _, value ->
            canvas.drawPath(value, paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                pathArray.append(event.getPointerId(event.actionIndex), Path().apply {
                    moveTo(event.getX(event.actionIndex), event.getY(event.actionIndex))
                })
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                pathArray.remove(event.getPointerId(event.actionIndex))
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                pathArray.forEach { id, path ->
                    val index = event.findPointerIndex(id)
                    // 下面的代码会和 MIUI 三指截屏冲突, 如果要真的使用加上 try catch 吧...
                    path.lineTo(event.getX(index), event.getY(index))
                }
                invalidate()
            }
        }
        return true
    }
}