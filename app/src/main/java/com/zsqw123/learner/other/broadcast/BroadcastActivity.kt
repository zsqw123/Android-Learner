package com.zsqw123.learner.other.broadcast

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActBroadcastBinding

class BroadcastActivity : AppCompatActivity() {
    private var perm: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActBroadcastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val broadcast = MyBroadcast(binding.tv)
        registerReceiver(broadcast, IntentFilter(BROADCAST_NAME))
        binding.apply {
            btSend.setOnClickListener {
                tv.text = ""
                if (perm != null)
                    sendOrderedBroadcast(Intent(BROADCAST_NAME).apply { putExtra("str", "This is a message from broadcast with perm!") }, BROADCAST_PERM)
                else sendBroadcast(Intent(BROADCAST_NAME).apply { putExtra("str", "This is a message from broadcast!") })
            }
            btPerm.setOnClickListener {
                perm = BROADCAST_PERM
            }
            btRemovePerm.setOnClickListener {
                perm = null
            }
        }
    }

    companion object {
        const val BROADCAST_NAME = "com.zsqw123.broadcast.test"
        const val BROADCAST_PERM = "com.zsqw123.broadcast.PERM"
    }
}