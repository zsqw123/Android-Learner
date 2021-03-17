package com.zsqw123.learner.view.touch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.view.*
import android.widget.OverScroller
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import kotlin.math.abs

class MyPager(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private val viewConfiguration = ViewConfiguration.get(context)
    private val vTracker = VelocityTracker.obtain()
    private val minV = viewConfiguration.scaledMinimumFlingVelocity
    private val maxV = viewConfiguration.scaledMaximumFlingVelocity
    private val pagingSlop = viewConfiguration.scaledPagingTouchSlop
    private var downX = 0f
    private var downY = 0f
    private var downScrollX = 0f
    private var scrolling = true
    private var overScroller = OverScroller(context)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var cL = 0
        var cR = width
        for (child in children) {
            child.layout(cL, 0, cR, height)
            cL += width
            cR += width
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            vTracker.clear() // 每次按下事件清空速度追踪器
        }
        vTracker.addMovement(ev) // 每次 MotionEvent 将初始坐标加入 vTracker 中用于之后计算
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                scrolling = false
                downScrollX = scrollX.toFloat()
                return false
            }
            MotionEvent.ACTION_MOVE -> if (!scrolling) {
                // 只有当滑动绝对值大于最小滑动距离时拦截事件, 之后交由 onTouchEvent 处理
                if (abs(ev.x - downX) > pagingSlop) {
                    scrolling = true // 避免多次触发
                    parent.requestDisallowInterceptTouchEvent(true) // 让父 view 不去拦截触摸事件
                    return true
                }
            }
        }
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                downScrollX = scrollX.toFloat()
            }
            MotionEvent.ACTION_MOVE -> {
                // 将所有子 view 移动 dx 的距离, 移动距离在 [0,width] 之间
                val dx = downX - event.x + downScrollX.coerceAtLeast(0f).coerceAtMost(width.toFloat())
                scrollTo(dx.toInt(), 0)
            }
            MotionEvent.ACTION_UP -> {
                vTracker.computeCurrentVelocity(1000, maxV.toFloat()) // 1000ms 内速度的改变量
                val dv = vTracker.xVelocity
                val x = scrollX
                val targetPage = if (abs(dv) < minV) { // 慢划的情况下, 滑动距离超过一半就跳到第2页, 否则第1页
                    if (x > width / 2) 1 else 0
                } else if (dv > 0) 1 else 0
                val scrollDistance = if (targetPage == 0) -x else width - x
                overScroller.startScroll(x, 0, scrollDistance, 0)
                postInvalidateOnAnimation()
            }
        }
        return true
    }

    override fun computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.currX, overScroller.currY)
            postInvalidateOnAnimation()
        }
    }
}

class TwoPagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(MyPager(this, null).apply {
            addView(View(context).apply {
                setBackgroundColor(Color.GRAY)
            })
            addView(View(context).apply {
                setBackgroundColor(Color.RED)
            })
        })
    }
}