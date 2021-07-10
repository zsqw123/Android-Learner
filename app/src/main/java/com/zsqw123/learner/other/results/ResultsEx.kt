package com.zsqw123.learner.other.results

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/10 13:38
 */

fun ActivityResultCaller.takePhoto(targetUri: Uri, callback: (Boolean) -> Unit) =
    registerForActivityResult(ActivityResultContracts.TakePicture(), callback).launch(targetUri)

fun ActivityResultCaller.takePhoto(callback: (Bitmap) -> Unit) =
    registerForActivityResult(ActivityResultContracts.TakePicturePreview(), callback).launch(null)

fun ActivityResultCaller.requestPermissions(vararg permissions: String, callback: (Map<String, Boolean>) -> Unit) =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), callback).launch(permissions)

fun ActivityResultCaller.takeVideo(targetUri: Uri, callback: (Bitmap) -> Unit) =
    registerForActivityResult(ActivityResultContracts.TakeVideo(), callback).launch(targetUri)

fun ActivityResultCaller.pickContact(callback: (Uri) -> Unit) =
    registerForActivityResult(ActivityResultContracts.PickContact(), callback).launch(null)

fun ActivityResultCaller.getContent(mimeType: String, callback: (Uri) -> Unit) =
    registerForActivityResult(ActivityResultContracts.GetContent(), callback).launch(mimeType)

fun ActivityResultCaller.createDocument(defaultName: String, callback: (Uri) -> Unit) =
    registerForActivityResult(ActivityResultContracts.CreateDocument(), callback).launch(defaultName)

fun ActivityResultCaller.openDocuments(vararg mimeType: String, callback: (List<Uri>) -> Unit) =
    registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments(), callback).launch(mimeType)

fun ActivityResultCaller.openDocument(vararg mimeType: String, callback: (Uri) -> Unit) =
    registerForActivityResult(ActivityResultContracts.OpenDocument(), callback).launch(mimeType)

fun ActivityResultCaller.openDocumentTree(defaultUri: Uri? = null, callback: (Uri) -> Unit) =
    registerForActivityResult(ActivityResultContracts.OpenDocumentTree(), callback).launch(defaultUri)

inline fun <reified C> ComponentActivity.startAndResult(vararg pairs: Pair<String, Any>, noinline callback: (ActivityResult) -> Unit) =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)
        .launch(Intent(this, C::class.java).apply { putExtras(bundleOf(*pairs)) })

inline fun <reified C> Fragment.startAndResult(vararg pairs: Pair<String, Any>, noinline callback: (ActivityResult) -> Unit) =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)
        .launch(Intent(context, C::class.java).apply { putExtras(bundleOf(*pairs)) })