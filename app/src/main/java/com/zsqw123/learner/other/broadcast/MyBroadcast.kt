package com.zsqw123.learner.other.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.TextView

class MyBroadcast(private val tv: TextView) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == BroadcastActivity.BROADCAST_NAME) {
            tv.text = intent.getStringExtra("str")
        }
    }
}