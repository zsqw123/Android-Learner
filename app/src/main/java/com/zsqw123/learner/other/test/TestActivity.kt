package com.zsqw123.learner.other.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.R
import com.zsqw123.learner.databinding.ActTestBinding


class TestActivity : AppCompatActivity() {
    private val binding by lazy { ActTestBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.bt.setOnClickListener {
//            supportFragmentManager.beginTransaction().add(R.id.frag_container, TestFragment()).commitAllowingStateLoss()

        }
    }

}