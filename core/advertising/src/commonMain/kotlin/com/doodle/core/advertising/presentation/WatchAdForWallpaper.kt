package com.doodle.core.advertising.presentation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.doodle.core.advertising.LocalAdvertisingManager
import com.doodle.core.advertising.enums.RewardedAdResult
import com.doodle.core.advertising.rememberRewardedAdViewState
import com.doodle.core.domain.enums.isNotPurchased
import com.doodle.core.navigation.args.PictureDetailsNavArgs
import com.doodle.core.ui.card.CardButton
import com.doodle.core.ui.state.LocalRemoveAdsStatus
import com.doodle.core.ui.state.rememberDialogState
import com.doodle.core.ui.tweenMedium
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import worldofwallpapers.core.advertising.generated.resources.Res
import worldofwallpapers.core.advertising.generated.resources.show_ad
import worldofwallpapers.core.advertising.generated.resources.watch_ad_for_wallpaper_title
import worldofwallpapers.core.ui.generated.resources.something_went_wrong

@Composable
fun WatchAdForWallpaper(
    modifier: Modifier = Modifier,
    args: PictureDetailsNavArgs?,
    onWatched: (PictureDetailsNavArgs?) -> Unit,
    onError: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val advertisingManager = LocalAdvertisingManager.current
    val removeAdStatus = LocalRemoveAdsStatus.current
    val rewardedInterstitialAd = rememberRewardedAdViewState(
        rewardedAdManager = advertisingManager.rewarded
    )
    val showAdToUser = rememberDialogState()
    val showDialogState = rememberDialogState(args != null)

    LaunchedEffect(showAdToUser.isVisible) {
        if (showAdToUser.isVisible) {
            rewardedInterstitialAd.showAd { result ->
                showAdToUser.dismiss()
                when (result) {
                    RewardedAdResult.REWARDED -> onWatched(args)
                    RewardedAdResult.ERROR -> {
                        scope.launch {
                            val msg = getString(worldofwallpapers.core.ui.generated.resources.Res.string.something_went_wrong)
                            onError(msg)
                        }
                    }

                    else -> onDismiss()
                }
            }
        }
    }

    LaunchedEffect(args){
        if(args != null){
            showDialogState.show()
        } else {
            showDialogState.dismiss()
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = showDialogState.isVisible,
        enter = fadeIn(tweenMedium()) + scaleIn(tweenMedium()),
        exit = scaleOut(tweenMedium()) + fadeOut()
    ) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = stringResource(Res.string.watch_ad_for_wallpaper_title),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CardButton(
                            onClick = {
                                if (removeAdStatus.isNotPurchased()) {
                                    showAdToUser.show()
                                } else {
                                    onWatched(args)
                                }
                            },
                        ) {
                            Text(
                                stringResource(Res.string.show_ad),
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    }
                }
            }
        }
    }

}