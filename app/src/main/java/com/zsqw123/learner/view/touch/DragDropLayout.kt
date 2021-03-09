package com.zsqw123.learner.view.touch

import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.view.forEach
import com.zsqw123.learner.databinding.ActDragDropBinding

private const val COLUMN = 3
private const val ROW = 2

class DragDropLayout(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private var anotherChildren: ArrayList<View> = arrayListOf()

    init {
        forEach { anotherChildren.add(it) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        measureChildren(
            MeasureSpec.makeMeasureSpec(w / ROW, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(h / COLUMN, MeasureSpec.EXACTLY)
        )
        setMeasuredDimension(w, h)
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

    override fun onFinishInflate() {
        super.onFinishInflate()
        children.forEach {
            it.setOnLongClickListener { v ->
                ViewCompat.startDragAndDrop(v, ClipData.newPlainText("", ""), DragShadowBuilder(v), v, DRAG_FLAG_GLOBAL)
                return@setOnLongClickListener false
            }
            it.setOnDragListener { v, event ->
                when (event.action) {
                    DragEvent.ACTION_DRAG_STARTED -> if (event.localState === v) {
                        v.visibility = View.INVISIBLE
                    }
                    DragEvent.ACTION_DRAG_ENDED -> if (event.localState == v) {
                        v.visibility = View.VISIBLE
                    }
                }
                return@setOnDragListener true
            }
        }

    }
}

class DragDropActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActDragDropBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}