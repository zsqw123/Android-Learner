package com.zsqw123.learner.view.anim

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Path
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import android.view.animation.PathInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.zsqw123.learner.databinding.ActAnimBinding
import com.zsqw123.learner.start
import com.zsqw123.learner.view.example.CameraActivity

class AnimActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActAnimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btAnim.setOnClickListener {
            ObjectAnimator.ofFloat(binding.myProgress, "count", 20f, 80f).apply {
                interpolator = OvershootInterpolator()
                duration = 4000
            }.start()
        }
        binding.btPath.setOnClickListener {
            ObjectAnimator.ofFloat(binding.myProgress, "count", 20f, 80f).apply {
                interpolator = PathInterpolator(Path().apply {
                    lineTo(0.1f, 0.3f)
                    lineTo(0.4f, 0.6f)
                    lineTo(1f, 1f)
                })
                duration = 4000
            }.start()
        }
        binding.btColor.setOnClickListener {
            ObjectAnimator.ofInt(binding.myProgress, "progressColor", Color.RED, Color.GREEN).apply {
                setEvaluator { fraction, startValue, endValue ->
                    val start = FloatArray(3)
                    val now = FloatArray(3)
                    val end = FloatArray(3)
                    Color.colorToHSV(startValue as Int, start)
                    Color.colorToHSV(endValue as Int, end)
                    now[0] = start[0] + (end[0] - start[0]) * fraction
                    now[1] = start[1] + (end[1] - start[1]) * fraction
                    now[2] = start[2] + (end[2] - start[2]) * fraction
                    return@setEvaluator Color.HSVToColor(now)
                }
                duration = 4000
            }.start()
        }
        binding.btRotate.setOnClickListener {
            start<CameraActivity>()
        }
    }
}