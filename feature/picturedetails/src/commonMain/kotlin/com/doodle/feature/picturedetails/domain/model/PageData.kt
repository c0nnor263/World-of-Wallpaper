package com.doodle.feature.picturedetails.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.doodle.core.domain.model.remote.RemoteImage

data class PageData(
    val image: MutableState<RemoteImage.Hit> = mutableStateOf(RemoteImage.Hit()),
//    val nativeAd: MutableState<NativeAd?> = mutableStateOf(null)
)
