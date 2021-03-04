package com.zsqw123.learner.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave

class CanvasTransformView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap: Bitmap
        get() = getXsBitmap(resources, 300)

    override fun onDraw(canvas: Canvas) {
        canvas.withSave {
            translate(100f, 100f)
            rotate(-45f, width / 2f, height / 2f)
            translate(-width / 2f, 0f)
            skew(0.5f, 0f)
            /** skew 会在原坐标的基础上进行叠加, 因此如果图片要放到中心的话必须错切完之后移动回中心
            float sx:将画布在x方向上倾斜相应的角度，sx倾斜角度的tan值，
            float sy:将画布在y轴方向上倾斜相应的角度，sy为倾斜角度的tan值.
            变换后:
            X = x + sx * y
            Y = y + sy * x
             */
            scale(2f, 3f, width / 2f, height / 2f)
            rotate(45f, width / 2f, height / 2f)
            drawBitmap(bitmap, width / 2 - 150f, height / 2 - 150f, paint)
        }
    }
}

class MatrixView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val mMatrix = Matrix()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap: Bitmap
        get() = getXsBitmap(resources, 300)

    override fun onDraw(canvas: Canvas) {
        mMatrix.setPolyToPoly(
            floatArrayOf(0f, 0f, 300f, 0f, 0f, 300f, 300f, 300f), 0,
            floatArrayOf(120f, 100f, 300f, 40f, 60f, 400f, 550f, 450f), 0, 4
        )
        canvas.withSave {
            concat(mMatrix)
            drawBitmap(bitmap, 0f, 0f, paint)
        }
    }
}

class CameraView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val cam = Camera()
    private val bitmap: Bitmap
        get() = getSquareBitmap(resources, 300)

    override fun onDraw(canvas: Canvas) {
        canvas.withSave {
            cam.save()
            translate(width / 2f, height / 2f)
            cam.rotateX(30f)
            cam.applyToCanvas(this)
            translate(-width / 2f, -height / 2f)
            cam.restore()
            drawBitmap(bitmap, width / 2f - 150, height / 2f - 150, paint)
        }
    }
}