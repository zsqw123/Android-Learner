package com.zsqw123.learner.other.results

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/10 13:38
 */

fun ComponentActivity.takePhoto() {
    registerForActivityResult(ActivityResultContracts.TakePicture()){res->

    }
}