package com.zsqw123.learner.other.results

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/10 13:38
 */

fun ComponentActivity.takePhoto(targetUri: Uri, callback: (Boolean) -> Unit) =
    registerForActivityResult(ActivityResultContracts.TakePicture(), callback).launch(targetUri)

fun ComponentActivity.takePhoto(callback: (Bitmap) -> Unit) =
    registerForActivityResult(ActivityResultContracts.TakePicturePreview(), callback).launch(null)

fun ComponentActivity.requestPermissions(vararg permissions: String, callback: (Map<String, Boolean>) -> Unit) =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), callback).launch(permissions)

fun ComponentActivity.takeVideo(targetUri: Uri, callback: (Bitmap) -> Unit) =
    registerForActivityResult(ActivityResultContracts.TakeVideo(), callback).launch(targetUri)

fun ComponentActivity.pickContact(callback: (Uri) -> Unit) =
    registerForActivityResult(ActivityResultContracts.PickContact(), callback).launch(null)

fun ComponentActivity.getContent(mimeType: String, callback: (Uri) -> Unit) =
    registerForActivityResult(ActivityResultContracts.GetContent(), callback).launch(mimeType)

fun ComponentActivity.createDocument(defaultName: String, callback: (Uri) -> Unit) =
    registerForActivityResult(ActivityResultContracts.CreateDocument(), callback).launch(defaultName)

fun ComponentActivity.openDocuments(vararg mimeType: String, callback: (List<Uri>) -> Unit) =
    registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments(), callback).launch(mimeType)

fun ComponentActivity.openDocument(vararg mimeType: String, callback: (Uri) -> Unit) =
    registerForActivityResult(ActivityResultContracts.OpenDocument(), callback).launch(mimeType)

fun ComponentActivity.openDocumentTree(defaultUri: Uri? = null, callback: (Uri) -> Unit) =
    registerForActivityResult(ActivityResultContracts.OpenDocumentTree(), callback).launch(defaultUri)

inline fun <reified C> ComponentActivity.startAndResult(noinline callback: (ActivityResult) -> Unit, vararg pairs: Pair<String, Any>) =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)
        .launch(Intent(this, C::class.java).apply { putExtras(bundleOf(*pairs)) })

