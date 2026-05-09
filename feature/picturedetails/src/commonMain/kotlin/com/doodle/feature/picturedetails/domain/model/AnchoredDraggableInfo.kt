package com.doodle.feature.picturedetails.domain.model

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import com.doodle.feature.picturedetails.domain.enums.DragAnchors

data class AnchoredDraggableInfo @OptIn(ExperimentalFoundationApi::class) constructor(
    val state: AnchoredDraggableState<DragAnchors>,
    val progress: Float,
    val isEnd: Boolean
)
