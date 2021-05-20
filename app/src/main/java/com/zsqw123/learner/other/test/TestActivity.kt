package com.zsqw123.learner.other.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActTestBinding
import com.zsqw123.learner.view.dp
import com.zsqw123.learner.view.getSquareBitmap

class TestActivity : AppCompatActivity() {
    private val binding by lazy { ActTestBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        binding.iv.setImageBitmap(getSquareBitmap(resources, 300.dp.toInt()))
    }
}