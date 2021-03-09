package com.zsqw123.learner.view.touch

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper
import com.zsqw123.learner.databinding.ActDragBinding

private const val COLUMN = 3
private const val ROW = 2

class DragLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private val dragHelper = ViewDragHelper.create(this, DragHelper())

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        measureChildren(
            MeasureSpec.makeMeasureSpec(w / ROW, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(h / COLUMN, MeasureSpec.EXACTLY)
        )
        setMeasuredDimension(w, h)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return dragHelper.shouldInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childW = width / ROW
        val childH = height / COLUMN
        var left: Int
        var top: Int
        children.forEachIndexed { index, child ->
            left = index % ROW * childW
            top = index / ROW * childH
            child.layout(left, top, left + childW, top + childH)
        }
    }

    inner class DragHelper : ViewDragHelper.Callback() {
        private var viewL = 0
        private var viewT = 0

        // 是否要允许抓起 这不废话吗?
        override fun tryCaptureView(child: View, pointerId: Int): Boolean = true

        // 限制横向位置 (clamp: 钳住)
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int = left

        // 限制纵向位置 (clamp: 钳住)
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int = top
        override fun onViewDragStateChanged(state: Int) {
            if (state == ViewDragHelper.STATE_IDLE) {
                dragHelper.capturedView?.elevation?.let { dragHelper.capturedView!!.elevation = it + 1 }
            }
        }

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            capturedChild.elevation = capturedChild.elevation + 1
            viewL = capturedChild.left
            viewT = capturedChild.top
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            dragHelper.settleCapturedViewAt(viewL, viewT)
            postInvalidateOnAnimation()
        }
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            postInvalidateOnAnimation()
        }
    }

}

class DragActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActDragBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}