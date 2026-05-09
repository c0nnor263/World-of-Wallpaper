package com.doodle.core.ui

//fun showShareDialog(context: Context, uri: Uri? = null, onClear: () -> Unit = {}) {
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
//
//
//
//@Composable
//fun PagingLaunchedEffect(
//    states: CombinedLoadStates,
//    onLoading: () -> Unit,
//    onError: (Throwable) -> Unit,
//    onNotLoading: () -> Unit
//) {
//    LaunchedEffect(states) {
//        val refreshState = states.refresh
//        val appendState = states.append
//
//        checkLoadState(
//            refreshState,
//            onLoading = onLoading,
//            onError = onError,
//            onNotLoading = {
//                checkLoadState(
//                    appendState,
//                    onLoading = onLoading,
//                    onError = onError,
//                    onNotLoading = onNotLoading
//                )
//            }
//        )
//    }
//}
//
//fun checkLoadState(
//    loadState: LoadState?,
//    onLoading: () -> Unit,
//    onError: (Throwable) -> Unit,
//    onNotLoading: () -> Unit
//) {
//    when (loadState) {
//        is LoadState.Loading -> onLoading()
//        is LoadState.Error -> onError(loadState.error)
//        is LoadState.NotLoading -> onNotLoading()
//        else -> onNotLoading()
//    }
//}
//
