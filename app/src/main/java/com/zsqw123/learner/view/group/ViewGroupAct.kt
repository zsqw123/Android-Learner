package com.zsqw123.learner.view.group

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActViewGroupBinding
import kotlin.concurrent.thread

class ViewGroupAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActViewGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}