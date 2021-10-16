package com.zsqw123.learner.other.intercept

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/10/16 10:26
 */
class OutterVG @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var startPos = 0f
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                startPos = ev.y
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                return ev.y - startPos > 30
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        println("outter event: ${event.actionMasked}")
        return true
    }
}

class InnerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    override fun onTouchEvent(event: MotionEvent): Boolean {
        println("inner event: ${event.actionMasked}")
        return true
    }
}