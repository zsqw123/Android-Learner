package com.zsqw123.learner.view.touch

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.zsqw123.learner.databinding.ActDragBinding

class DragLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        measureChildren(width / 2, width / 2)
        var i = 0
        var cT = 0
        for (child in children) {
            val cL = if (i % 2 == 0) 0 else width / 2
            if (i % 2 == 0 && i != 0) cT += width / 2
            child.layout(cL, cT, cL + width / 2, cT + height / 2)
            i++
        }
        setMeasuredDimension(width, height)
    }
}

class DragActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActDragBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}