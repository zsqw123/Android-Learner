package com.zsqw123.learner.view.touch

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActDragListenerBinding

class TouchInterceptActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindng = ActDragListenerBinding.inflate(layoutInflater)
        setContentView(bindng.root)
    }
}

class ViewA(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    init {
        addView(ViewB(context, attrs))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> false
            MotionEvent.ACTION_UP -> true
            else -> false
        }.apply { if (this) println("A") }
    }
}

class ViewB(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    init {
        addView(ViewC(context, attrs))
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_UP -> false
            else -> false
        }.apply { if (this) println("B") }
    }
}

class ViewC(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> false
            MotionEvent.ACTION_UP -> true
            else -> false
        }.apply { if (this) println("C") }
    }
}