package com.doodle.feature.home.presentation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.doodle.core.advertising.presentation.WatchAdForWallpaper
import com.doodle.core.navigation.Screens
import com.doodle.core.navigation.args.PictureDetailsNavArgs
import com.doodle.core.navigation.args.SearchNavArgs
import com.doodle.core.ui.ApplicationScaffold
import com.doodle.core.ui.LoadingBar
import com.doodle.core.ui.NavigationIcon
import com.doodle.core.ui.captureChildrenGestures
import com.doodle.core.ui.platformDisplayCutoutPadding
import com.doodle.core.ui.state.rememberDialogState
import com.doodle.core.ui.tweenMedium
import com.doodle.feature.home.domain.enums.TabPage
import com.doodle.feature.home.presentation.common.HomeNavigationDrawer
import com.doodle.feature.home.presentation.common.HomePager
import com.doodle.feature.home.presentation.common.HomeTabLayout
import com.doodle.feature.home.state.LocalHomePagingState
import com.doodle.feature.home.state.rememberHomeDrawerState
import kotlinx.coroutines.flow.filter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import worldofwallpapers.core.ui.generated.resources.Res
import worldofwallpapers.core.ui.generated.resources.app_name
import worldofwallpapers.core.ui.generated.resources.menu_24px
import worldofwallpapers.core.ui.generated.resources.search_24px
import kotlin.math.abs
import kotlin.time.Duration.Companion.seconds

fun NavGraphBuilder.homeScreen(
    onNavigateToFavorites: () -> Unit,
    onNavigateToSearch: (SearchNavArgs?) -> Unit,
    onNavigateToDetails: (PictureDetailsNavArgs) -> Unit
) {
    composable<Screens.Home>(
        enterTransition = {
            fadeIn(tweenMedium())
        },
        exitTransition = { fadeOut(tweenMedium()) }
    ) {
        val viewModel: HomeScreenViewModel = koinViewModel()
        HomeScreen(
            viewModel = viewModel,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToDetails = onNavigateToDetails,
            onNavigateToFavorites = onNavigateToFavorites
        )
    }
}


@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = koinViewModel(),
    onNavigateToFavorites: () -> Unit,
    onNavigateToSearch: (SearchNavArgs?) -> Unit,
    onNavigateToDetails: (PictureDetailsNavArgs) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val drawerState = rememberHomeDrawerState()
    val showLoadingDialogState = rememberDialogState()

    LaunchedEffect(drawerState.state) {
        snapshotFlow { drawerState.state.currentOffset }
            .filter { !it.isNaN() && it != 0f }
            .collect { offset ->
                drawerState.updateWidth(abs(offset))
            }

    }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        HomeNavigationDrawer(
            drawerState = drawerState.state,
            onNavigateToFavorites = onNavigateToFavorites,
            onShowReview = {
//                val activity = context as ComponentActivity
//                viewModel.requestApplicationReview(activity)
            },
            onRequestRemoveAds = {
//                showLoadingDialogState.showFor(10)
//                val activity = context as ComponentActivity
//                viewModel.requestBillingRemoveAds(activity, onError = {
//                    val msg = context.getString(
//                        com.doodle.core.ui.R.string.something_went_wrong
//                    )
//                    viewModel.updateUiState(HomeScreenViewModel.UiState.Error(msg))
//                    showLoadingDialogState.dismiss()
//                })
            },
            onRestorePurchases = {
                showLoadingDialogState.showFor(5.seconds)
                viewModel.restorePurchases()
            },
            modifier = Modifier.graphicsLayer {
                val interpolatedScale = lerp(
                    start = 0.8F, stop = 1F, fraction = drawerState.fraction
                )
                scaleX = interpolatedScale
                scaleY = interpolatedScale
            }
        )
        ApplicationScaffold(
            title = stringResource(Res.string.app_name),
            navigationIcon = {
                NavigationIcon(
                    painter = painterResource(Res.drawable.menu_24px),
                    onClick = {
                        drawerState.showOrClose()
                    }
                )
            },
            actions = arrayOf({
                IconButton(onClick = {
                    onNavigateToSearch(null)
                }) {
                    Icon(
                        painter = painterResource(Res.drawable.search_24px),
                        contentDescription = null
                    )
                }
            }),
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val interpolatedScale = lerp(
                        start = 1F, stop = 1.2F, fraction = drawerState.fraction
                    )
                    val interpolatedTransitionX = lerp(
                        start = 0F,
                        stop = drawerState.width * 1.15F,
                        fraction = drawerState.fraction
                    )
                    translationX = interpolatedTransitionX
                    scaleX = interpolatedScale
                    scaleY = interpolatedScale

                    if (drawerState.fraction > 0F) {
                        shadowElevation = 24F
                        shape = RoundedCornerShape(24.dp)
                        clip = true
                    }
                }
                .border(
                    width = if (drawerState.fraction > 0f) 1.dp else 0.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(24.dp)
                )
                .captureChildrenGestures(drawerState.state.isOpen) {
                    drawerState.close()
                }
                .platformDisplayCutoutPadding()
        ) { innerPadding ->
            CompositionLocalProvider(LocalHomePagingState provides viewModel.homePagingState) {
                HomeScreenContent(
                    uiState = uiState,
                    onUpdateUiState = viewModel::updateUiState,
                    onNavigateToDetails = onNavigateToDetails,
                    onNavigateToSearch = onNavigateToSearch,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding())
                )
            }
        }
    }

    WatchAdForWallpaper(
        args = (uiState as? HomeScreenViewModel.UiState.Premium)?.args,
        onWatched = { args ->
            args?.let { onNavigateToDetails(it) }
            viewModel.updateUiState(null)
        },
        onError = { msg ->
            viewModel.updateUiState(HomeScreenViewModel.UiState.Error(msg))
        },
        onDismiss = {
            viewModel.updateUiState(null)
        },
    )

    LoadingBar(visible = showLoadingDialogState.isVisible)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    onUpdateUiState: (HomeScreenViewModel.UiState?) -> Unit,
    onNavigateToSearch: (SearchNavArgs) -> Unit,
    onNavigateToDetails: (PictureDetailsNavArgs) -> Unit,
    uiState: HomeScreenViewModel.UiState?,
) {
    val homePagingState = LocalHomePagingState.current
    val pagerState = rememberPagerState(initialPage = TabPage.LATEST.ordinal) {
        TabPage.entries.size
    }

    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeTabLayout(
            modifier = Modifier.fillMaxWidth(),
            pagerState = pagerState,
            isLoading = uiState is HomeScreenViewModel.UiState.Loading,
            errorMsg = (uiState as? HomeScreenViewModel.UiState.Error)?.message,
            onErrorClick = {
                homePagingState.retry()
            })

        val primary = MaterialTheme.colorScheme.primary.copy(0.1F)
        val secondary = MaterialTheme.colorScheme.secondary.copy(0.1F)
        val colors = remember {
            listOf(
                primary,
                Color.Transparent,
                Color.Transparent,
                secondary
            )
        }
        HomePager(
            pagerState = pagerState,
            onNavigateToDetails = onNavigateToDetails,
            onNavigateToSearch = onNavigateToSearch,
            onUpdateUiState = onUpdateUiState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1F)
                .background(brush = Brush.linearGradient(colors = colors))
        )
    }
}
