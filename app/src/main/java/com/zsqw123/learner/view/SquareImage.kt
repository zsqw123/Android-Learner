package com.zsqw123.learner.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

class IconImageView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val size = (75..150).random().dp.toInt()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG) apply {
        textSize = 20.dp
        textAlign = Paint.Align.CENTER
        color = Color.BLACK
    }
    private val bitmap = getSquareBitmap(resources, size)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(size, size + 20.dp.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.drawText("Hello", width / 2f, size + 20.dp.toInt() - textPaint.descent(), textPaint)
    }
}

class ChangeableGroup(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private val childRectList = arrayListOf<Rect>()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthUsed = 0
        var singleWidthUsed = 0
        var heightUsed = 0
        var singleHeightUsed = 0
        children.forEachIndexed { index, child ->
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED && singleWidthUsed + child.measuredWidth > MeasureSpec.getSize(widthMeasureSpec)) {
                singleWidthUsed = 0
                heightUsed += singleHeightUsed
                singleHeightUsed = 0
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            }
            if (index >= childRectList.size) {
                childRectList.add(Rect())
            }
            val childRect = childRectList[index]
            childRect.set(singleWidthUsed, heightUsed, singleWidthUsed + child.measuredWidth, heightUsed + measuredHeight)
            singleWidthUsed += child.measuredWidth
            widthUsed = maxOf(widthUsed, singleWidthUsed)
            singleHeightUsed = maxOf(singleHeightUsed, child.measuredHeight)
        }
        setMeasuredDimension(widthUsed, heightUsed + singleHeightUsed)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEachIndexed { index, child ->
            child.layout(childRectList[index].left, childRectList[index].top, childRectList[index].right, childRectList[index].bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}