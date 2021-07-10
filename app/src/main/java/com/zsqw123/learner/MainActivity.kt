package com.zsqw123.learner

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zsqw123.learner.databinding.ActivityMainBinding
import com.zsqw123.learner.other.broadcast.BroadcastActivity
import com.zsqw123.learner.other.permission.storage.StorageAct
import com.zsqw123.learner.other.simpleglide.SimpleGlideActivity
import com.zsqw123.learner.other.test.TestActivity
import com.zsqw123.learner.view.anim.AnimActivity
import com.zsqw123.learner.view.group.ViewGroupAct
import com.zsqw123.learner.view.photoview.PhotoAct
import com.zsqw123.learner.view.touch.TouchActivity
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {
    private val activityArr = arrayOf<KClass<out Activity>>(
        AnimActivity::class, ViewGroupAct::class, PhotoAct::class, TouchActivity::class, ServiceActivity::class,
        BroadcastActivity::class, SimpleGlideActivity::class, StorageAct::class, TestActivity::class
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            rv.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            rv.adapter = ButtonRvActivtyAdapter(this@MainActivity, activityArr)
        }
    }
}