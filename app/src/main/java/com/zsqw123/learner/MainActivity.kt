package com.zsqw123.learner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActivityMainBinding
import com.zsqw123.learner.other.broadcast.BroadcastActivity
import com.zsqw123.learner.other.simpleglide.SimpleGlideActivity
import com.zsqw123.learner.view.photoview.PhotoAct
import com.zsqw123.learner.view.anim.AnimActivity
import com.zsqw123.learner.view.group.ViewGroupAct
import com.zsqw123.learner.view.touch.TouchActivity
//import okhttp3.*
//import java.io.IOException
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btAnim.setOnClickListener { start(AnimActivity::class) }
            btViewGroup.setOnClickListener { start(ViewGroupAct::class) }
            btPhoto.setOnClickListener { start(PhotoAct::class) }
            btTouch.setOnClickListener { start(TouchActivity::class) }
            btService.setOnClickListener { start(ServiceActivity::class) }
            btBroadcast.setOnClickListener { start(BroadcastActivity::class) }
            btSimpleGlide.setOnClickListener { start(SimpleGlideActivity::class) }
        }

//        val okHttpClient = OkHttpClient.Builder().build()
//        val request = Request.Builder().url("").get().build()
//        val call = okHttpClient.newCall(request)
//        call.enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {}
//            override fun onResponse(call: Call, response: Response) {}
//        })
    }
}

fun Activity.start(newActivity: KClass<*>) = startActivity(Intent(this, newActivity.java))