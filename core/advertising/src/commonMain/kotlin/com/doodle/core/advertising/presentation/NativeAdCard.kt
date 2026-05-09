package com.doodle.core.advertising.presentation
//
//import android.annotation.SuppressLint
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.appcompat.widget.AppCompatButton
//import androidx.cardview.widget.CardView
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.scaleOut
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.wrapContentSize
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import com.doodle.core.advertising.R
//import com.doodle.core.ui.tweenMedium
//import com.google.android.gms.ads.nativead.MediaView
//import com.google.android.gms.ads.nativead.NativeAd
//import com.google.android.gms.ads.nativead.NativeAdView
//
//@SuppressLint("InflateParams")
//@Composable
//fun NativeAdCard(
//    modifier: Modifier = Modifier,
//    nativeAd: NativeAd?,
//    isAdDismissed: Boolean,
//    onDismiss: () -> Unit
//) {
//    AnimatedVisibility(
//        modifier = modifier,
//        visible = nativeAd != null && isAdDismissed.not(),
//        exit = scaleOut(tweenMedium()) + fadeOut(tweenMedium())
//    ) {
//        Card(
//            modifier = Modifier.wrapContentSize(),
//            elevation = CardDefaults.cardElevation(4.dp),
//            colors = CardDefaults.cardColors(Color.DarkGray),
//            border = BorderStroke(2.dp, MaterialTheme.colorScheme.inversePrimary)
//        ) {
//            AndroidView(
//                modifier = Modifier.padding(2.dp),
//                factory = { context ->
//                    val layoutInflater = LayoutInflater.from(context)
//                    layoutInflater.inflate(R.layout.native_ad_card, null) as NativeAdView
//                }
//            ) { view ->
//                if (nativeAd == null) {
//                    return@AndroidView
//                }
//
//                // MediaView
//                view.mediaView = view.findViewById<MediaView>(R.id.media_view).apply {
//                    mediaContent = nativeAd.mediaContent
//                    view.findViewById<CardView>(R.id.media_view_card).visibility = View.VISIBLE
//                    setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//                }
//
//                // Ad
//                view.advertiserView = view.findViewById<TextView>(R.id.ad_notification_view).apply {
//                    text = nativeAd.advertiser
//                }
//
//                // Icon
//                view.iconView = view.findViewById<ImageView>(R.id.icon).apply {
//                    setImageDrawable(nativeAd.icon?.drawable)
//                }
//
//                // Headline
//                view.headlineView = view.findViewById<TextView>(R.id.primary).apply {
//                    text = nativeAd.headline
//                }
//
//                // Secondary Headline
//                view.bodyView = view.findViewById<TextView>(R.id.body).apply {
//                    text = nativeAd.body
//                }
//
//                // Call to Action
//                view.callToActionView = view.findViewById<AppCompatButton>(R.id.cta).apply {
//                    text = nativeAd.callToAction
//                }
//
//                view.setNativeAd(nativeAd)
//
//                view.findViewById<AppCompatButton>(R.id.dismiss_btn).setOnClickListener {
//                    onDismiss()
//                }
//            }
//        }
//    }
//}
