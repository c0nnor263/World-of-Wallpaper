package com.doodle.turboracing3.presentation.composables


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.crossfade
import com.doodle.core.advertising.LocalAdvertisingManager
import com.doodle.core.advertising.appopen.rememberAppOpenAdViewState
import com.doodle.core.domain.enums.RemoveAdsStatus
import com.doodle.core.domain.enums.isNotPurchased
import com.doodle.core.navigation.Screens
import com.doodle.core.navigation.isPermittedForAppOpenAd
import com.doodle.core.ui.DisposableEffectLifecycle
import com.doodle.core.ui.state.LocalRemoveAdsStatus
import com.doodle.core.ui.theme.WallpapersTheme
import com.doodle.core.ui.tweenEasy
import com.doodle.turboracing3.presentation.AppContentViewModel
import com.doodle.turboracing3.presentation.navigation.AppHost
import okio.Path
import org.koin.compose.viewmodel.koinViewModel

expect fun imageCacheDirectory(context: PlatformContext): Path

@Composable
fun AppContent() {
    val viewModel = koinViewModel<AppContentViewModel>()
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()

    val isAvailableForAppOpenAd =
        viewModel.isAvailableForAppOpenAd.collectAsStateWithLifecycle(false)
    val advertisingManager = LocalAdvertisingManager.current
    val appOpenAdState = rememberAppOpenAdViewState(
        appOpenAdManager = advertisingManager.appOpen,
    )

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(imageCacheDirectory(context))
                    .maxSizePercent(0.02)
                    .build()
            }
            .build()
    }

    val removeAdsStatus =
        viewModel.isPremiumUser.collectAsStateWithLifecycle(RemoveAdsStatus.NOT_PURCHASED)

    DisposableEffectLifecycle(
        onResume = {
            viewModel.onResumeBilling()
            val isAvailableForAppOpenAd = isAvailableForAppOpenAd.value
            if (removeAdsStatus.value.isNotPurchased() &&
                backStackEntry.value.isPermittedForAppOpenAd() &&
                isAvailableForAppOpenAd
            ) {
                appOpenAdState.showAd()
            }
        },
        onDestroy = {
            viewModel.destroyNativeAds()
        }
    )

    CompositionLocalProvider(
        LocalRemoveAdsStatus provides removeAdsStatus.value
    ) {
        WallpapersTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(tweenEasy()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppHost(
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )

                BottomBarContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = WindowInsets.systemGestures.asPaddingValues()
                                .calculateLeftPadding(
                                    LayoutDirection.Ltr
                                )
                        ),
                    isVisible = backStackEntry.value?.destination?.hasRoute<Screens.Splash>() == false &&
                            removeAdsStatus.value.isNotPurchased()
                )

            }
        }
    }
}
