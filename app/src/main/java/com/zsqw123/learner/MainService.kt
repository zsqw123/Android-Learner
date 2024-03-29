package com.zsqw123.learner

import android.app.Notification
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.LayoutServiceBinding

class MainService : Service() {
    private fun toastOnService() = toast("awa")
    inner class MyBinder : Binder() {
        fun callServiceMethod() = toastOnService()
        fun fore() = startForeground(1, Notification())
    }

    override fun onBind(intent: Intent): IBinder = MyBinder()
}

class ServiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LayoutServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var isConnected = false
        val myConnection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                isConnected = false
                binding.btCall.setOnClickListener {}
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                service as MainService.MyBinder
                binding.btCall.setOnClickListener {
                    if (isConnected)
                        service.callServiceMethod()
                    service.fore()
                }
            }
        }
        binding.btStart.setOnClickListener {
            startService(Intent(this, MainService::class.java))
            isConnected = true
            toast("服务已开启!")
        }
        binding.btBind.setOnClickListener {
            bindService(Intent(this, MainService::class.java), myConnection, Service.BIND_AUTO_CREATE)
            isConnected = true
            toast("已绑定服务!")
        }
        binding.btUnbind.setOnClickListener {
            if (isConnected) {
                isConnected = false
                unbindService(myConnection)
                toast("服务已解绑!")
            }
        }
        binding.btEnd.setOnClickListener {
            stopService(Intent(this, MainService::class.java))
            isConnected = false
            toast("已杀死服务!")
        }
    }
}

//fun newKt(){
//    fun changeBackground(color: Color){}
//    val blue = Color(255)
//    changeBackground(blue)
//}