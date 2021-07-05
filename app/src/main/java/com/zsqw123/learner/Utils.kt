package com.zsqw123.learner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlin.reflect.KClass

inline fun <reified T> Activity.start(vararg pairs: Pair<String, Any>) {
    startActivity(Intent(this, T::class.java).apply { putExtras(bundleOf(*pairs)) })
}

fun Activity.start(clazz: Class<out Activity>, vararg pairs: Pair<String, Any>) {
    startActivity(Intent(this, clazz).apply { putExtras(bundleOf(*pairs)) })
}

fun toast(string: String, context: Context = app) = Toast.makeText(context, string, Toast.LENGTH_SHORT).show()

fun Any?.prl() = println(this)

class ButtonRvActivtyAdapter(private val activity: Activity, private val arr: Array<KClass<out Activity>>) :
    ButtonRvAdapter(arr.size, { i, v ->
        v.text = arr[i].java.simpleName
        v.setOnClickListener { activity.start(arr[i].java) }
    })

open class ButtonRvAdapter(val count: Int, val event: (Int, MaterialButton) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    override fun getItemCount(): Int = count
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = MainHolder(MaterialButton(parent.context))
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder.itemView as MaterialButton).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            event(position, this)
        }
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}