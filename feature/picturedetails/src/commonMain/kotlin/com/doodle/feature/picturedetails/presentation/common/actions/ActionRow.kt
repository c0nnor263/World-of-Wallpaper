package com.doodle.feature.picturedetails.presentation.common.actions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doodle.core.domain.enums.ActionType
import com.doodle.core.ui.ActionButton
import com.doodle.core.ui.ActionButtonWithImage
import com.doodle.core.ui.SetWallpapersActionButton
import com.doodle.core.ui.theme.WallpapersTheme
import com.doodle.core.ui.tweenMedium
import com.doodle.feature.picturedetails.state.LocalFavoriteIconEnabled
import worldofwallpapers.core.ui.generated.resources.Res
import worldofwallpapers.core.ui.generated.resources.download_24px
import worldofwallpapers.core.ui.generated.resources.favorite_24px
import worldofwallpapers.core.ui.generated.resources.favorite_border_24px
import worldofwallpapers.core.ui.generated.resources.mobile_share_24px

const val PictureDetailsScreenContentShareTag = "PictureDetailsScreenContentShareTag"
const val PictureDetailsScreenContentFavoriteTag = "PictureDetailsScreenContentFavoriteTag"
const val PictureDetailsScreenContentDownloadTag = "PictureDetailsScreenContentDownloadTag"
const val PictureDetailsScreenContentPublisherInfoTag =
    "PictureDetailsScreenContentPublisherInfoTag"

@Composable
fun ActionRow(
    modifier: Modifier = Modifier,
    visible: Boolean,
    isActive: Boolean,
    userImageUrl: String,
    onActionClick: (ActionType) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current
    val isFavoriteIconEnabled = LocalFavoriteIconEnabled.current

    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = slideInVertically(tweenMedium()) { it } + scaleIn(tweenMedium()),
        exit = scaleOut(tweenMedium()) + slideOutVertically(tweenMedium()) { it }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SetWallpapersActionButton(
                isActive = isActive,
                onActionClick = onActionClick
            )
            Spacer(Modifier.height(4.dp))
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    Color.Black.copy(0.4F)
                )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    ActionButton(
                        iconDrawableResource = if (isFavoriteIconEnabled) {
                           Res.drawable.favorite_24px
                        } else {
                           Res.drawable.favorite_border_24px
                        },
                        type = ActionType.FAVORITE,
                        onActionClick = {
                            onActionClick(it)
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        color = if (isFavoriteIconEnabled) Color.Red else Color.White,
                        isActive = isActive,
                        modifier = Modifier.testTag(PictureDetailsScreenContentFavoriteTag)
                    )
                    ActionButton(
                        iconDrawableResource = Res.drawable.download_24px,
                        type = ActionType.DOWNLOAD,
                        onActionClick = onActionClick,
                        isActive = isActive,
                        modifier = Modifier.testTag(PictureDetailsScreenContentDownloadTag)
                    )
                    ActionButton(
                        iconDrawableResource = Res.drawable.mobile_share_24px,
                        type = ActionType.SHARE,
                        onActionClick = onActionClick,
                        isActive = isActive,
                        modifier = Modifier.testTag(PictureDetailsScreenContentShareTag)
                    )
                    ActionButtonWithImage(
                        imageUrl = userImageUrl,
                        type = ActionType.PUBLISHER_INFO,
                        onActionClick = onActionClick,
                        isActive = isActive,
                        modifier = Modifier.testTag(PictureDetailsScreenContentPublisherInfoTag)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ActionRowPreview() {
    WallpapersTheme {

        ActionRow(
            visible = true,
            isActive = true,
            userImageUrl = "",
            onActionClick = {}
        )
    }
}
