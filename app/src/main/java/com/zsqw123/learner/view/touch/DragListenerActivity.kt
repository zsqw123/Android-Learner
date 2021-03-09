package com.zsqw123.learner.view.touch

import android.os.Bundle
import android.provider.MediaStore
import android.view.DragEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActDragListenerBinding

class DragListenerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindng=ActDragListenerBinding.inflate(layoutInflater)
        setContentView(bindng.root)
        bindng.root.setOnDragListener { v, event ->
            if (event.action == DragEvent.ACTION_DROP) {
//                bindng.tv.text = event.clipData.getItemAt(0).text
                val uri = event.clipData.getItemAt(0).uri
//                bindng.iv.setImageURI(uri)
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

                bindng.iv.setImageBitmap(bitmap)
                println("=====================" + event.clipData.description)
            }
            return@setOnDragListener true
        }
    }
}