package com.zsqw123.learner.other.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Handler
import android.os.Looper
import android.util.SparseArray
import androidx.annotation.IntDef
import androidx.core.util.forEach
import androidx.core.util.set
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/12 11:12
 */
class NetCallback private constructor() : ConnectivityManager.NetworkCallback() {
    private val netStates = BooleanArray(4)
    private fun onChange() {
        Handler(Looper.getMainLooper()).post { events.forEach { _, v -> v(netStates) } }
    }

    override fun onAvailable(network: Network) {
        netStates[NetType.NET_AVAILABLE] = true
    }

    override fun onLost(network: Network) {
        netStates[NetType.NET_CONNECTED] = false
        netStates[NetType.NET_WIFI] = false
        netStates[NetType.NET_CELLULAR] = false
        onChange()
    }

    override fun onUnavailable() {
        netStates[NetType.NET_AVAILABLE] = false
        netStates[NetType.NET_CONNECTED] = false
        netStates[NetType.NET_WIFI] = false
        netStates[NetType.NET_CELLULAR] = false
        onChange()
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        netStates[NetType.NET_CONNECTED] = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        netStates[NetType.NET_WIFI] = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        netStates[NetType.NET_CELLULAR] = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        onChange()
    }

    companion object {
        private val events = SparseArray<(BooleanArray) -> Unit>()
        private var count = 0
        private val netRequest by lazy { NetworkRequest.Builder().build() }
        private lateinit var connManager: ConnectivityManager
        fun init(application: Application) {
            connManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connManager.registerNetworkCallback(netRequest, NetCallback())
        }

        internal fun unregist(index: Int) = events.delete(index)
        internal fun regist(callback: (BooleanArray) -> Unit): Int {
            events[count] = callback
            return count++
        }
    }
}

@IntDef(NetType.NET_AVAILABLE, NetType.NET_CONNECTED, NetType.NET_WIFI, NetType.NET_CELLULAR)
@Retention(AnnotationRetention.SOURCE)
annotation class NetType {
    companion object {
        const val NET_AVAILABLE = 0
        const val NET_CONNECTED = 1
        const val NET_WIFI = 2
        const val NET_CELLULAR = 3
    }
}

/**
 * 自动跟随生命周期的网络回调，需要实现了 LifecycleOwner 接口
 * @receiver LifecycleOwner
 */
fun LifecycleOwner.bindNetworkCallback(callback: (BooleanArray) -> Unit) {
    lifecycle.addObserver(object : LifecycleEventObserver {
        var callbackIndex = 0
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_START -> callbackIndex = NetCallback.regist(callback)
                Lifecycle.Event.ON_STOP -> NetCallback.unregist(callbackIndex)
                else -> Unit
            }
        }
    })
}

/**
 * 注册全局回调，会返回回调事件的索引，可以用来解除注册回调事件
 * @see unregistGlobalNetworkCallback
 */
fun registGlobalNetworkCallback(callback: (BooleanArray) -> Unit): Int = NetCallback.regist(callback)

/**
 * 解除回调事件
 * @param callbackIndex Int
 * @see registGlobalNetworkCallback
 */
fun unregistGlobalNetworkCallback(callbackIndex: Int) = NetCallback.unregist(callbackIndex)