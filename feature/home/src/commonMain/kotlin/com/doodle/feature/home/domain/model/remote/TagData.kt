package com.doodle.feature.home.domain.model.remote

import androidx.compose.runtime.MutableState
import com.doodle.core.domain.model.remote.RemoteImage

data class TagData(
    val title: String,
    val image: MutableState<RemoteImage.Hit?>
)