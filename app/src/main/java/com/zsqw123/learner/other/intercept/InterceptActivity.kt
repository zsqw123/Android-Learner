package com.zsqw123.learner.other.intercept

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActInterceptBinding

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/10/16 10:20
 */
class InterceptActivity : AppCompatActivity() {
    private val binding by lazy { ActInterceptBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}