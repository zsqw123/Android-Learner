package com.zsqw123.learner

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zsqw123.learner.databinding.ActivityMainBinding
import com.zsqw123.learner.other.broadcast.BroadcastActivity
import com.zsqw123.learner.other.simpleglide.SimpleGlideActivity
import com.zsqw123.learner.other.simpleglide.sampleImageUrl
import com.zsqw123.learner.view.anim.AnimActivity
import com.zsqw123.learner.view.group.ViewGroupAct
import com.zsqw123.learner.view.photoview.PhotoAct
import com.zsqw123.learner.view.touch.TouchActivity
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import java.io.IOException
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
//        val retrofit = Retrofit.Builder().baseUrl("https://cdn.jsdelivr.net").build()
//        val test = retrofit.create(RetrofitTest::class.java)
//        test.getPic().enqueue(object :Callback<BitmapAdapter>{
//            override fun onResponse(call: Call<BitmapAdapter>, response: Response<BitmapAdapter>) {
//            }
//
//            override fun onFailure(call: Call<BitmapAdapter>, t: Throwable) {
//            }
//
//        })
//        OkHttpClient.Builder().build().newCall(Request.Builder().url("https://www.baidu.com").build()).enqueue(object : okhttp3.Callback {
//            override fun onFailure(call: okhttp3.Call, e: IOException) {
//            }
//
//            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
//            }
//        })
//        RecyclerView(this)
//        Glide.with(this).load(sampleImageUrl).submit().get()
//        Handler(Looper.getMainLooper())
    }
}

fun Activity.start(newActivity: KClass<*>) = startActivity(Intent(this, newActivity.java))

interface RetrofitTest {
    @GET()
    fun getPic(): Call<BitmapAdapter>
}

class BitmapAdapter