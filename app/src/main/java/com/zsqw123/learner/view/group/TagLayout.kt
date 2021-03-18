package com.zsqw123.learner.view.group

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.children
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import com.zsqw123.learner.databinding.LayoutTagBinding
import com.zsqw123.learner.view.apply
import com.zsqw123.learner.view.dp
import com.zsqw123.learner.view.enlargeShuffled

private val strList = listOf("一个", "两个字", "三个字呢", "这是四个字").enlargeShuffled(11)
private val colorList = arrayOf(Color.RED, Color.GREEN, Color.GRAY, Color.BLUE)

/**
 * 另一个范例, 另一个范例在 onMeasure 里创建对象, 并不优雅 qwq
 * @see ChangeableGroup
 */
class TagLayout(context: Context, attrs: AttributeSet? = null) : ViewGroup(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthUsed = 0
        var singleWidthUsed = 0
        var heightUsed = 0
        var singleHeightUsed = 0
        children.forEach { child ->
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED && singleWidthUsed + child.measuredWidth > MeasureSpec.getSize(widthMeasureSpec)) {
                singleWidthUsed = 0
                heightUsed += singleHeightUsed
                singleHeightUsed = 0
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            }
            singleWidthUsed += child.measuredWidth
            widthUsed = maxOf(widthUsed, singleWidthUsed)
            singleHeightUsed = maxOf(singleHeightUsed, child.measuredHeight)
        }
        setMeasuredDimension(widthUsed, heightUsed + singleHeightUsed)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var maxHeight = 0
        var top = 0
        var left = 0
        children.forEach { child ->
            maxHeight = maxHeight.coerceAtLeast(child.measuredHeight)
            left += child.marginStart
            if (left + child.measuredWidth + child.marginEnd > r - l) {
                top += maxHeight
                left = child.marginStart
            }
            child.layout(left, top, left + child.measuredWidth + child.marginEnd, top + child.measuredHeight)
            left += child.measuredWidth + child.marginEnd
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}

class SingleTagView(context: Context, attrs: AttributeSet? = null) : AppCompatTextView(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = colorList.random() }

    init {
        text = " ${strList.random()} "
        textSize = 6.dp
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), 8.dp, 8.dp, paint)
        super.onDraw(canvas)
    }
}

class TagAct : AppCompatActivity() {
    val binding by lazy { LayoutTagBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        repeat(strList.size) {
            val single = SingleTagView(this)
            single.layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                setMargins(12, 12, 12, 12)
            }
            binding.tag.addView(single, single.layoutParams)
        }
    }
}