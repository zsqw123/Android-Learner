package com.zsqw123.learner.view.multitouch

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActTouchBinding

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
        }
    }
}