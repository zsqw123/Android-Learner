package com.zsqw123.learner.other.test

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zsqw123.learner.databinding.ActTestBinding
import com.zsqw123.learner.prl
import com.zsqw123.learner.start
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

class TestActivity : AppCompatActivity() {
    private val binding by lazy { ActTestBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            repeat(100){
                delay(200)

                AWA().toString().prl()
            }

        }
        binding.bt.setOnClickListener {
            start<TestActivity>("1" to AWA())
        }

    }
}

@Parcelize
class AWA(var a: Int = 11) : Parcelable
