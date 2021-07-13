package com.zsqw123.learner.other.results

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.SparseArray
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.util.set
import androidx.fragment.app.Fragment

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/7/10 13:38
 */
val ActivityResultCaller.pendingResults: SparseArray<(Any) -> Unit> by lazy { SparseArray() }
val ActivityResultCaller.pendingLaunches: SparseArray<ActivityResultLauncher<*>> by lazy { SparseArray() }


private fun ActivityResultCaller.unionCallBack(idx: Int): (Any) -> Unit {
    return { it -> pendingResults[idx, {}](it) }
}

fun ActivityResultCaller.initResults() {
    pendingLaunches[0] = registerForActivityResult(ActivityResultContracts.TakePicture(), unionCallBack(0))
    pendingLaunches[1] = registerForActivityResult(ActivityResultContracts.TakePicturePreview(), unionCallBack(1))
    pendingLaunches[2] = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), unionCallBack(2))
    pendingLaunches[3] = registerForActivityResult(ActivityResultContracts.TakeVideo(), unionCallBack(3))
    pendingLaunches[4] = registerForActivityResult(ActivityResultContracts.PickContact(), unionCallBack(4))
    pendingLaunches[5] = registerForActivityResult(ActivityResultContracts.GetContent(), unionCallBack(5))
    pendingLaunches[6] = registerForActivityResult(ActivityResultContracts.CreateDocument(), unionCallBack(6))
    pendingLaunches[7] = registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments(), unionCallBack(7))
    pendingLaunches[8] = registerForActivityResult(ActivityResultContracts.OpenDocument(), unionCallBack(8))
    pendingLaunches[9] = registerForActivityResult(ActivityResultContracts.OpenDocumentTree(), unionCallBack(9))
    pendingLaunches[10] = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), unionCallBack(10))
    pendingLaunches[11] = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), unionCallBack(11))
}

// 0
@Suppress("UNCHECKED_CAST")
fun ActivityResultCaller.takePhoto(targetUri: Uri, callback: (Boolean) -> Unit) {
    pendingResults[0] = { callback(it as Boolean) }
    (pendingLaunches[0] as ActivityResultLauncher<Uri>).launch(targetUri)
}

// 1
fun ActivityResultCaller.takePhoto(callback: (Bitmap) -> Unit) {
    pendingResults[1] = { callback(it as Bitmap) }
    pendingLaunches[1].launch(null)
}

// 2
@Suppress("UNCHECKED_CAST")
fun ActivityResultCaller.requestPermissions(vararg permissions: String, callback: (Map<String, Boolean>) -> Unit) {
    pendingResults[2] = { callback(it as Map<String, Boolean>) }
    (pendingLaunches[2] as ActivityResultLauncher<Array<out String>>).launch(permissions)
}

// 3
@Suppress("UNCHECKED_CAST")
fun ActivityResultCaller.takeVideo(targetUri: Uri, callback: (Bitmap) -> Unit) {
    pendingResults[3] = { callback(it as Bitmap) }
    (pendingLaunches[3] as ActivityResultLauncher<Uri>).launch(targetUri)
}

// 4
fun ActivityResultCaller.pickContact(callback: (Uri) -> Unit) {
    pendingResults[4] = { callback(it as Uri) }
    pendingLaunches[4].launch(null)
}

// 5
@Suppress("UNCHECKED_CAST")
fun ActivityResultCaller.getContent(mimeType: String, callback: (Uri) -> Unit) {
    pendingResults[5] = { callback(it as Uri) }
    (pendingLaunches[5] as ActivityResultLauncher<String>).launch(mimeType)
}

// 6
@Suppress("UNCHECKED_CAST")
fun ActivityResultCaller.createDocument(defaultName: String, callback: (Uri) -> Unit) {
    pendingResults[6] = { callback(it as Uri) }
    (pendingLaunches[6] as ActivityResultLauncher<String>).launch(defaultName)
}

// 7
@Suppress("UNCHECKED_CAST")
fun ActivityResultCaller.openDocuments(vararg mimeType: String, callback: (List<Uri>) -> Unit) {
    pendingResults[7] = { callback(it as List<Uri>) }
    (pendingLaunches[7] as ActivityResultLauncher<Array<out String>>).launch(mimeType)
}

// 8
@Suppress("UNCHECKED_CAST")
fun ActivityResultCaller.openDocument(vararg mimeType: String, callback: (Uri) -> Unit) {
    pendingResults[8] = { callback(it as Uri) }
    (pendingLaunches[8] as ActivityResultLauncher<Array<out String>>).launch(mimeType)
}

// 9
@Suppress("UNCHECKED_CAST")
fun ActivityResultCaller.openDocumentTree(defaultUri: Uri? = null, callback: (Uri) -> Unit) {
    pendingResults[9] = { callback(it as Uri) }
    (pendingLaunches[9] as ActivityResultLauncher<Uri>).launch(defaultUri)
}

// 10
@Suppress("UNCHECKED_CAST")
inline fun <reified C> ComponentActivity.startAndResult(vararg pairs: Pair<String, Any>, noinline callback: (ActivityResult) -> Unit) {
    pendingResults[10] = { callback(it as ActivityResult) }
    (pendingLaunches[10] as ActivityResultLauncher<Intent>).launch(Intent(this, C::class.java).apply { putExtras(bundleOf(*pairs)) })
}

// 11
@Suppress("UNCHECKED_CAST")
inline fun <reified C> Fragment.startAndResult(vararg pairs: Pair<String, Any>, noinline callback: (ActivityResult) -> Unit) {
    pendingResults[11] = { callback(it as ActivityResult) }
    (pendingLaunches[11] as ActivityResultLauncher<Intent>).launch(Intent(context, C::class.java).apply { putExtras(bundleOf(*pairs)) })
}