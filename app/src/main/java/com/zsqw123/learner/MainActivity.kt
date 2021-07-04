package com.zsqw123.learner

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.zsqw123.learner.databinding.ActivityMainBinding
import com.zsqw123.learner.other.broadcast.BroadcastActivity
import com.zsqw123.learner.other.simpleglide.SimpleGlideActivity
import com.zsqw123.learner.other.test.TestActivity
import com.zsqw123.learner.view.anim.AnimActivity
import com.zsqw123.learner.view.group.ViewGroupAct
import com.zsqw123.learner.view.photoview.PhotoAct
import com.zsqw123.learner.view.touch.TouchActivity

class MainActivity : AppCompatActivity() {
    private val activityArr = arrayOf(
        AnimActivity::class, ViewGroupAct::class, PhotoAct::class, TouchActivity::class, ServiceActivity::class,
        BroadcastActivity::class, SimpleGlideActivity::class, TestActivity::class
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            rv.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            rv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
                override fun getItemCount(): Int = activityArr.size
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = MainHolder(MaterialButton(parent.context))
                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    (holder.itemView as MaterialButton).apply {
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        text = activityArr[position].java.simpleName
                        setOnClickListener { start(activityArr[position].java) }
                    }
                }

                inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
            }
        }
    }
}