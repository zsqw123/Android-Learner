package com.zsqw123.learner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.os.bundleOf
import kotlin.reflect.KClass

inline fun <reified T> Activity.start(vararg pairs: Pair<String, Any>) {
    startActivity(Intent(this, T::class.java).apply { putExtras(bundleOf(*pairs)) })
}

fun Activity.start(clazz: Class<out Activity>, vararg pairs: Pair<String, Any>) {
    startActivity(Intent(this, clazz).apply { putExtras(bundleOf(*pairs)) })
}

fun toast(string: String, context: Context = app) = Toast.makeText(context, string, Toast.LENGTH_SHORT).show()

fun Any?.prl() = println(this)
