package com.zsqw123.learner.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

// Shader 究竟还是 Shader, 貌似对于 BitmapShader
// 不能指定从什么位置开始 Bitmap 的绘制啊, 难道说就只是一种类似于瓷砖铺设的设计吗?
// 或许等之后学会了移动 canvas 的之后就可以了吧
class BitmapComposeShaderView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // CLAMP 使用边缘着色
    // MIRROR 镜像原图
    // REPEAT 重复
    private val backgroundShader = BitmapShader(getRectBitmap(resources, 300), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR)
    private val xparentShader = BitmapShader(getXsBitmap(resources, 200), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
    private val composeShader = ComposeShader(backgroundShader, xparentShader, PorterDuff.Mode.DST_OUT)


    override fun onDraw(canvas: Canvas) {
        paint.shader = backgroundShader
        canvas.drawCircle(width / 2f, height / 2f - 400f, 400f, paint)
        paint.shader = xparentShader
        canvas.drawRect(width / 2f - 200f, height / 2f - 600f, width / 2f + 200f, height / 2f - 200f, paint)
        setLayerType(LAYER_TYPE_SOFTWARE, null) // 使用 composeShader 里面是同一种 shader 的时候需要关闭硬件加速
        paint.shader = composeShader
        canvas.drawCircle(width / 2f, height / 2f + 440f, 400f, paint)
    }
}