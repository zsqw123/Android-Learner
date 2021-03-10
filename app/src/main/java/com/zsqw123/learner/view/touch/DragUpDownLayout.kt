package com.zsqw123.learner.view.touch

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper
import com.zsqw123.learner.databinding.ActDragUpdownBinding
import kotlin.math.abs

const val COUNTS = 3

class DragUpDownLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private val dragHelper = ViewDragHelper.create(this, DragHelperCallback())
    private val viewConfiguration = ViewConfiguration.get(context)
    private val minV = viewConfiguration.scaledMinimumFlingVelocity
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        for (c in children) c.measure(
            MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(h / COUNTS, MeasureSpec.EXACTLY)
        )
        setMeasuredDimension(w, h)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val itemHeight = height / COUNTS
        for ((i, c) in children.withIndex()) {
            c.layout(0, i * itemHeight, width, (i + 1) * itemHeight)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return dragHelper.shouldInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    inner class DragHelperCallback : ViewDragHelper.Callback() {
        private var toUpper = false

        override fun tryCaptureView(child: View, pointerId: Int): Boolean = true
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int = top
        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            when (state) {
                ViewDragHelper.STATE_IDLE -> toUpper = dragHelper.capturedView!!.top > height / 2
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            if (abs(yvel) < minV && releasedChild.top + releasedChild.bottom < height) {
                toUpper = !toUpper
            }

            dragHelper.settleCapturedViewAt(0, if (toUpper) 0 else height - releasedChild.height)
            postInvalidateOnAnimation()
        }
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            postInvalidateOnAnimation()
        }
    }
}

class DragUpDownActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActDragUpdownBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}