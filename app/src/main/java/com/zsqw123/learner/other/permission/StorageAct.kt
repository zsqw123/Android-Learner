package com.zsqw123.learner.other.permission

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zsqw123.learner.databinding.ActPermBinding
import com.zsqw123.learner.other.permission.storage.getPicUris
import com.zsqw123.learner.other.permission.storage.save.ImageSave
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/4 10:50
 */
class StorageAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActPermBinding.inflate(layoutInflater).apply {
            setContentView(root)
            btReadPic.setOnClickListener {
                lifecycleScope.launch(Dispatchers.Main) {
                    rvPic.adapter = PicRvAdapter(contentResolver.getPicUris())
                }
            }
            rvPic.layoutManager = GridLayoutManager(this@StorageAct, 3, RecyclerView.VERTICAL, false)
        }
    }
}