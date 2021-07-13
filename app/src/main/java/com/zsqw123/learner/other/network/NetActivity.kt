package com.zsqw123.learner.other.network

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActNetBinding

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/12 11:09
 */
class NetActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActNetBinding.inflate(layoutInflater).apply {
            setContentView(root)
            bindNetworkCallback {
                tvNet.text = "isAvailable=${it[0]}\nisConnected=${it[1]}\nisWifi=${it[2]}\nisCellular=${it[3]}"
            }
        }
    }
}