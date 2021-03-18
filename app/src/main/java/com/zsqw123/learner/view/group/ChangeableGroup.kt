package com.zsqw123.learner.view.group

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children

/**
 * 更好的实现
 * @see TagLayout
 */
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