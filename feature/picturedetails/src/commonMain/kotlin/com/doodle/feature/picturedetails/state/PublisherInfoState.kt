package com.doodle.feature.picturedetails.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.doodle.feature.picturedetails.domain.model.PublisherInfoData

@Composable
fun rememberPublisherInfoState(): PublisherInfoState {
    return remember {
        PublisherInfoState()
    }
}

class PublisherInfoState {
    var isVisible: Boolean by mutableStateOf(false)
    var info: PublisherInfoData? by mutableStateOf(null)

    fun setPublisherData(data: PublisherInfoData) {
        info = data
    }

    fun show() {
        isVisible = true
    }

    fun hide() {
        isVisible = false
    }
}
