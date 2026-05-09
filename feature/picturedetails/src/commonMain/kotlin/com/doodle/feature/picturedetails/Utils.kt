package com.doodle.feature.picturedetails

//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.widget.Toast
//import com.doodle.core.ui.R
//
//fun showShareDialog(context: Context, uri: Uri?, onClear: () -> Unit) {
//    val appName = context.getString(com.doodle.core.domain.R.string.app_name)
//    val bundle = context.packageName
//
//    val intent = Intent(Intent.ACTION_SEND).apply {
//        type = "image/*"
//        putExtra(
//            Intent.EXTRA_TEXT,
//            context.getString(
//                R.string.share_app_text_content,
//                appName,
//                bundle
//            )
//        )
//        uri?.let { putExtra(Intent.EXTRA_STREAM, uri) }
//    }
//
//    val intentChooser = Intent.createChooser(
//        intent,
//        context.getString(R.string.share_text_title)
//    )
//    context.startActivity(intentChooser)
//    onClear()
//}
//
//fun showToast(context: Context, message: String) {
//    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//}
