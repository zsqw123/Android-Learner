package com.zsqw123.learner.view.example

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.withSave
import com.zsqw123.learner.databinding.ActivityCameraBinding
import com.zsqw123.learner.view.ViewInvalidateDelegates
import com.zsqw123.learner.view.distance
import com.zsqw123.learner.view.dp
import com.zsqw123.learner.view.getSquareBitmap
import kotlin.concurrent.thread
import kotlin.math.atan2

class CameraAnimView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getSquareBitmap(resources, 200.dp.toInt())
    private val camera = Camera()
    private var angle by ViewInvalidateDelegates { 0f }
    private var rotateXAngle by ViewInvalidateDelegates { 15f }
    override fun onDraw(canvas: Canvas) {
        canvas.withSave {
            canvas.translate(width / 2f, height / 2f)
            canvas.rotate(-angle)
            camera.save()
            camera.rotateX(rotateXAngle)
            camera.applyToCanvas(this)
            camera.restore()
            clipRect((-200).dp, 0f, 200.dp, 200.dp)
            rotate(angle)
            drawBitmap(bitmap, (-100).dp, (-100).dp, paint)
        }
        canvas.withSave {
            rotate(-angle, width / 2f, height / 2f)
            clipRect(0, 0, width, height / 2)
            rotate(angle, width / 2f, height / 2f)
            drawBitmap(bitmap, width / 2 - 100.dp, height / 2 - 100.dp, paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                rotateXAngle = 15f * distance(width / 2f, height / 2f, event.x, event.y) / 300f
                angle = Math.toDegrees(atan2(event.x.toDouble() - width / 2, event.y.toDouble() - height / 2)).toFloat()
            }
        }
        return true
    }
}

class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        thread {
            Thread.sleep(1000)
            runOnUiThread { ObjectAnimator.ofFloat(binding.camView, "angle", 0f, 720f).setDuration(2000).start() }
        }
    }
}