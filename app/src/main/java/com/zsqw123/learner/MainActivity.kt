package com.zsqw123.learner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActivityMainBinding
import com.zsqw123.learner.view.anim.AnimActivity
import com.zsqw123.learner.view.anim.ViewGroupAct
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btAnim.setOnClickListener { start(AnimActivity::class) }
        binding.btViewGroup.setOnClickListener { start(ViewGroupAct::class) }
    }
}

fun Activity.start(newActivity: KClass<*>) = startActivity(Intent(this, newActivity.java))