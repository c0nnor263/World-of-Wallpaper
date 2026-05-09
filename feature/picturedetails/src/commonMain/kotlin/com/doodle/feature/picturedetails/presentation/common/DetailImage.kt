package com.doodle.feature.picturedetails.presentation.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.doodle.core.ui.tweenMedium
import com.doodle.feature.picturedetails.domain.model.ActionRowClickData
import com.doodle.feature.picturedetails.domain.model.PageData
import com.doodle.feature.picturedetails.presentation.PictureDetailsScreenContentImage
import com.doodle.feature.picturedetails.presentation.PictureDetailsViewModel
import com.doodle.feature.picturedetails.presentation.common.actions.ActionRow
import com.doodle.feature.picturedetails.state.LocalPictureDetailsUiState
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailImage(
    modifier: Modifier = Modifier,
    pageData: PageData,
    isActiveNow: Boolean,
    onActionClick: (ActionRowClickData) -> Unit,
    onNavigateBack: () -> Unit,
    onDismissAd: () -> Unit,
    onImageStateChanged: (AsyncImagePainter.State) -> Unit
) {
    val pictureDetailsUiState = LocalPictureDetailsUiState.current
    var imageStateBuffer by remember {
        mutableStateOf<AsyncImagePainter.State?>(null)
    }
    var localDiskCacheKey by remember {
        mutableStateOf<String?>(null)
    }
    val context = LocalPlatformContext.current
    val imageLoader = remember { SingletonImageLoader.get(context) }

    var isAdDismissed by remember { mutableStateOf(false) }
    val isAdNotExists = true
//        pageData.nativeAd.value == null || isAdDismissed
    val isActionRowEnabled =
        isActiveNow &&
                isAdNotExists &&
                pictureDetailsUiState == PictureDetailsViewModel.UiState.ImageStateLoaded ||
                pictureDetailsUiState == null
    // TODO: Think about simplifying the logic for this
    LaunchedEffect(isActiveNow, imageStateBuffer) {
        if (isActiveNow) {
            imageStateBuffer?.let {
                onImageStateChanged(it)
            }
        }
    }


// TODO: Consider adding a top to bottom slide
    AnchoredDraggableArea(
        modifier = modifier,
        onTopEnd = onNavigateBack
    ) { draggableInfo ->
        val isDragReachedThreshold = draggableInfo.progress < 0.035F
        val clipRoundedShapeAnimation by animateDpAsState(
            targetValue = if (isDragReachedThreshold) 0.dp else 24.dp,
            animationSpec = tweenMedium(),
            label = ""
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(pageData.image.value.largeImageURL)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
                imageLoader = imageLoader,
                onState = { state ->
                    if (state is AsyncImagePainter.State.Success) {
                        localDiskCacheKey = state.result.diskCacheKey
                    }
                    imageStateBuffer = state
                },
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(PictureDetailsScreenContentImage)
                    .scale(1f - draggableInfo.progress)
                    .offset {
                        IntOffset(
                            x = 0,
                            y = draggableInfo.state
                                .requireOffset()
                                .roundToInt()
                        )
                    }
                    .anchoredDraggable(
                        state = draggableInfo.state,
                        orientation = Orientation.Vertical,
                        enabled = isActiveNow && isAdNotExists
                    )
                    .clip(
                        RoundedCornerShape(
                            bottomStart = clipRoundedShapeAnimation,
                            bottomEnd = clipRoundedShapeAnimation
                        )
                    )
                    .then(
                        if (!isAdNotExists) {
                            Modifier.blur(10.dp, 10.dp)
                        } else {
                            Modifier
                        }
                    )

            )

//            NativeAdCard(
//                nativeAd = pageData.nativeAd.value,
//                isAdDismissed = isAdDismissed
//            ) {
//                isAdDismissed = true
//                onDismissAd()
//            }

            ActionRow(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                isActive = isActionRowEnabled,
                visible = isDragReachedThreshold,
                userImageUrl = pageData.image.value?.userImageURL ?: "",
                onActionClick = {
                    if (isActiveNow) {
                        val clickData = ActionRowClickData(
                            type = it,
                            image = pageData.image.value,
                            diskCacheKey = localDiskCacheKey
                        )
                        onActionClick(clickData)
                    }
                }
            )
        }
    }
}
