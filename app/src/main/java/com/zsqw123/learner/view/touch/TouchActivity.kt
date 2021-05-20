package com.zsqw123.learner.view.touch

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActTouchBinding
import com.zsqw123.learner.start

class TouchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActTouchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btPainterSwitch.setOnClickListener {
                if (viewFocus.visibility == View.VISIBLE) {
                    viewFocus.visibility = View.GONE
                    viewPainter.visibility = View.VISIBLE
                } else {
                    viewFocus.visibility = View.VISIBLE
                    viewPainter.visibility = View.GONE
                }
            }
            btTwoPager.setOnClickListener { start<TwoPagerActivity>() }
            btDrag.setOnClickListener { start<DragActivity>() }
            btDragDrop.setOnClickListener { start<DragDropActivity>() }
            btTouchIntercept.setOnClickListener { start<TouchInterceptActivity>() }
            btDragUpdown.setOnClickListener { start<DragUpDownActivity>() }
        }
    }
}