package com.zsqw123.learner.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActPhotoBinding

class PhotoAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}