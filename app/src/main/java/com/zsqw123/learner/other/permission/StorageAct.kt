package com.zsqw123.learner.other.permission

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zsqw123.learner.ButtonRvAdapter
import com.zsqw123.learner.databinding.ActPermBinding
import com.zsqw123.learner.other.permission.storage.getPicUris
import com.zsqw123.learner.other.permission.storage.save.FileSave
import com.zsqw123.learner.other.permission.storage.save.ImageSave
import com.zsqw123.learner.toast
import com.zsqw123.learner.view.dp
import com.zsqw123.learner.view.getSquareBitmap
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
            rvBt.layoutManager = LinearLayoutManager(this@StorageAct, RecyclerView.VERTICAL, false)
            rvPic.layoutManager = GridLayoutManager(this@StorageAct, 3, RecyclerView.VERTICAL, false)
            val bts: Array<Pair<String, () -> Unit>> = arrayOf(
                "ReadPic" to {
                    lifecycleScope.launch(Dispatchers.Main) {
                        rvPic.adapter = PicRvAdapter(contentResolver.getPicUris())
                    }
                },
                "WritePic" to {
                    lifecycleScope.launch {
                        val res = ImageSave(getSquareBitmap(resources, 200.dp.toInt())).save()
                        if (res) toast("保存成功") else toast("保存失败")
                    }
                },
                "WriteFile" to {
                    lifecycleScope.launch {
                        val res = FileSave("hhhhhc").save()
                        if (res) toast("保存成功") else toast("保存失败")
                    }
                },
            )
            rvBt.adapter = ButtonRvAdapter(bts.size) { i, v ->
                v.text = bts[i].first
                v.setOnClickListener { bts[i].second() }
            }
        }
    }
}