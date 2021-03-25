package com.zsqw123.learner.other.simpleglide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActGlideBinding

class SimpleGlideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActGlideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.load.setOnClickListener {
            SGlide(this).load { bitmap -> binding.iv.setImageBitmap(bitmap) }
        }
    }
}