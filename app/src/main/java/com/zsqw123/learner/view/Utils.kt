package com.zsqw123.learner.view

import android.content.res.Resources
import android.util.TypedValue

val Int.px
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
    )