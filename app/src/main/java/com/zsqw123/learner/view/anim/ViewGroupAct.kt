package com.zsqw123.learner.view.anim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActViewGroupBinding

class ViewGroupAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActViewGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}