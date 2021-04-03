package com.zsqw123.learner.other.simpleglide

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActGlideBinding

val sampleImageUrl = "https://cdn.jsdelivr.net/gh/zsqw123/cdn@master/picCDN/20210323225931.png"

class SimpleGlideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActGlideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            et.setText(sampleImageUrl)
            load.setOnClickListener {
                SGlide(this@SimpleGlideActivity, et.text.toString()).load { bitmap ->
                    binding.iv.setImageBitmap(bitmap)
                    binding.tv.text = "LoadFrom: " + SGlide.loadFrom::name.get()
                }
            }
            clearCache.setOnClickListener {
                Cache(this@SimpleGlideActivity).clearAll()
            }
            clearImage.setOnClickListener { iv.setImageBitmap(null) }
            clearWeakref.setOnClickListener { WeakRefLoader.weakHashMap.clear() }
            clearLru.setOnClickListener { LruLoder.lru.evictAll() }
        }
    }
}