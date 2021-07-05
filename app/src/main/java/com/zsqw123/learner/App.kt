package com.zsqw123.learner

import android.app.Application
import android.content.Context
import com.zsqw123.learner.other.permission.storage.storageInit

lateinit var app: App

class App : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        app = this
        storageInit(app)
    }
}