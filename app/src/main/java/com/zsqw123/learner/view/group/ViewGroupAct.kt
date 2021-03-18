package com.zsqw123.learner.view.group

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActViewGroupBinding
import com.zsqw123.learner.start

class ViewGroupAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActViewGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btTag.setOnClickListener {
            start(TagAct::class)
        }
    }
}
