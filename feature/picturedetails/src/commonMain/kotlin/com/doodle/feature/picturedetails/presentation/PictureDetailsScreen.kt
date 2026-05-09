package com.doodle.feature.picturedetails.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import com.doodle.core.domain.enums.PaginatorKey
import com.doodle.core.domain.enums.isPurchased
import com.doodle.core.domain.source.PlatformFileExporter
import com.doodle.core.navigation.Screens
import com.doodle.core.navigation.args.PictureDetailsNavArgs
import com.doodle.core.navigation.args.SearchNavArgs
import com.doodle.core.ui.LoadingBar
import com.doodle.core.ui.state.LocalRemoveAdsStatus
import com.doodle.core.ui.tweenMedium
import com.doodle.feature.picturedetails.domain.model.ActionRowClickData
import com.doodle.feature.picturedetails.presentation.common.DetailImage
import com.doodle.feature.picturedetails.presentation.common.dialog.PublisherInfoDialog
import com.doodle.feature.picturedetails.state.LocalFavoriteIconEnabled
import com.doodle.feature.picturedetails.state.LocalPictureDetailsUiState
import com.doodle.feature.picturedetails.state.PagerPictureDetailState
import com.doodle.feature.picturedetails.state.rememberPagerDetailState
import com.doodle.feature.picturedetails.state.rememberPublisherInfoState
import org.koin.compose.viewmodel.koinViewModel

const val PictureDetailsScreenTag = "PictureDetailsScreenTag"
const val PictureDetailsScreenContentImage = "PictureDetailsScreenContentImage"


fun NavGraphBuilder.detailsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSearch: (SearchNavArgs?) -> Unit
) {
    composable<Screens.Details>(
        enterTransition = {
            fadeIn(animationSpec = tweenMedium()) +
                    scaleIn(animationSpec = tweenMedium()) +
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tweenMedium()
                    )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                tweenMedium()
            ) +
                    scaleOut(animationSpec = tweenMedium()) +
                    fadeOut(animationSpec = tweenMedium())
        }
    ) { backStackEntry ->
        val details = backStackEntry.toRoute<Screens.Details>()
        // TODO: Consider adding mappers
        val pictureDetailsNavArgs = PictureDetailsNavArgs(
            selectedImageIndex = details.selectedImageIndex,
            paginatorKey = PaginatorKey.valueOf(details.pagingKey),
            searchQuery = details.query,
            isPremium = details.isPremium
        )

        val pictureDetailsViewModel: PictureDetailsViewModel = koinViewModel()
        PictureDetailsScreen(
            viewModel = pictureDetailsViewModel,
            navArgs = pictureDetailsNavArgs,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateBack = onNavigateBack
        )
    }
}

@Composable
fun PictureDetailsScreen(
    viewModel: PictureDetailsViewModel = koinViewModel(),
    navArgs: PictureDetailsNavArgs,
    onNavigateToSearch: (SearchNavArgs?) -> Unit,
    onNavigateBack: () -> Unit
) {
    val removeAdsStatus = LocalRemoveAdsStatus.current
    val platformContext = LocalPlatformContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    var loadingDownloadDialogVisible by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val publisherInfoState = rememberPublisherInfoState()

    LaunchedEffect(Unit) {
        viewModel.setPagingData(navArgs)
    }

    LaunchedEffect(uiState.value) {
        when (val state = uiState.value) {
            PictureDetailsViewModel.UiState.Initializing -> {
                loadingDownloadDialogVisible = true
            }
            PictureDetailsViewModel.UiState.Ready -> {
                loadingDownloadDialogVisible = false
            }
            PictureDetailsViewModel.UiState.NoMoreFavorites -> {
                //                val message = context.getString(R.string.no_more_favorites)
//                showToast(context, message)
                onNavigateBack()
            }

            PictureDetailsViewModel.UiState.ImageStateLoading,
            PictureDetailsViewModel.UiState.Actions.StartSavingPictureToDevice
            -> {
                loadingDownloadDialogVisible = true
            }

            is PictureDetailsViewModel.UiState.ImageStateLoaded -> {
                loadingDownloadDialogVisible = false
            }

            is PictureDetailsViewModel.UiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.clearUiState()
            }

            is PictureDetailsViewModel.UiState.Actions.ExportFileToDownloads -> {
                PlatformFileExporter.exportFile(
                    path = state.path,
                    onExported = {
                        viewModel.updateUiState(
                            PictureDetailsViewModel.UiState.Actions.FinishedSavingPictureToDevice
                        )
                    },
                    onCancelled = {
                        loadingDownloadDialogVisible = false
                        viewModel.clearUiState()
                    },
                    onFailed = { message ->
                        loadingDownloadDialogVisible = false
                        viewModel.updateUiState(
                            PictureDetailsViewModel.UiState.Error(message)
                        )
                    }
                )
            }

            PictureDetailsViewModel.UiState.Actions.FinishedSavingPictureToDevice -> {
//                val message = context.getString(R.string.downloaded)
//                showToast(context, message)
                loadingDownloadDialogVisible = false
                viewModel.clearUiState()
            }

            PictureDetailsViewModel.UiState.Actions.SavedPictureToFavorites -> {
//                val message = context.getString(R.string.saved_to_favorites)
//                showToast(context, message)
                viewModel.clearUiState()
            }

            PictureDetailsViewModel.UiState.Actions.RemovedPictureFromFavorites -> {
//                val message = context.getString(R.string.removed_from_favorites)
//                showToast(context, message)
                viewModel.clearUiState()
            }

            is PictureDetailsViewModel.UiState.Actions.Share -> {
//                showShareDialog(context, state.uri) {
                    viewModel.clearUiState()
//                }
            }

            is PictureDetailsViewModel.UiState.Actions.ShowPublisherInfo -> {
                publisherInfoState.apply {
                    setPublisherData(state.data)
                    show()
                }
                viewModel.clearUiState()
            }

            is PictureDetailsViewModel.UiState.Actions.SetWallpaper -> {
//                val intent = Intent(Intent.ACTION_ATTACH_DATA).run {
//                    addCategory(Intent.CATEGORY_DEFAULT)
//                    setDataAndType(state.uri, "image/*")
//                    putExtra(Intent.EXTRA_MIME_TYPES, "image/*")
//                    Intent.createChooser(this, "Set as:")
//                }
//                context.startActivity(intent)
                viewModel.clearUiState()
            }

            null -> {}
        }
    }

    CompositionLocalProvider(
        LocalFavoriteIconEnabled provides viewModel.pictureDetailsState.isFavorite,
        LocalPictureDetailsUiState provides uiState.value
    ) {
        if(uiState.value is PictureDetailsViewModel.UiState.Initializing) return@CompositionLocalProvider
        val paginator = viewModel.paginator
        val pagerDetailState = rememberPagerDetailState(
            initialPage = navArgs.selectedImageIndex,
            isScrollEnabled = !navArgs.isPremium || removeAdsStatus.isPurchased(),
            paginator = paginator,
//        onGetNativeAd = viewModel::getNativeAdById,
            onCheckFavorite = viewModel::checkForFavorite,
            onDismissAd = viewModel::dismissNativeAd
        )
        PictureDetailsScreenContent(
            pagerState = pagerDetailState,
            onNavigateBack = onNavigateBack,
            onImageStateChanged = { state ->
                when (state) {
                    is AsyncImagePainter.State.Success -> {
                        val newState = PictureDetailsViewModel.UiState.ImageStateLoaded
                        viewModel.updateUiState(newState)
                    }

                    is AsyncImagePainter.State.Loading -> {
                        viewModel.updateUiState(PictureDetailsViewModel.UiState.ImageStateLoading)
                    }

                    is AsyncImagePainter.State.Error -> {
                        val message = state.result.throwable.message ?: ""
//                            ?: context.getString(R.string.error)
                        viewModel.updateUiState(PictureDetailsViewModel.UiState.Error(message))
                    }

                    is AsyncImagePainter.State.Empty -> {}

                    else -> {}
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .testTag(PictureDetailsScreenTag),
            onActionClick = { clickData ->
                viewModel.onActionClick(platformContext, clickData)
            }
        )
    }

    LoadingBar(visible = loadingDownloadDialogVisible)

    PublisherInfoDialog(
        state = publisherInfoState,
        onTagSearch = { tag ->
            onNavigateToSearch(
                SearchNavArgs(
                    query = tag
                )
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PictureDetailsScreenContent(
    modifier: Modifier = Modifier,
    pagerState: PagerPictureDetailState,
    onNavigateBack: () -> Unit,
    onImageStateChanged: (AsyncImagePainter.State) -> Unit,
    onActionClick: (ActionRowClickData) -> Unit
) {
    val removeAdsStatus = LocalRemoveAdsStatus.current
    HorizontalPager(
        modifier = modifier,
        state = pagerState.pagerState,
        key = pagerState::getKey,
        userScrollEnabled = pagerState.isScrollEnabled,
    ) { pageIndex ->
        val pageData = remember { pagerState.getPageData(pageIndex, removeAdsStatus) }
        val isActiveNow by remember {
            derivedStateOf {
                pagerState.isActiveNow(pageIndex)
            }
        }

        DetailImage(
            pageData = pageData,
            isActiveNow = isActiveNow,
            onActionClick = onActionClick,
            onNavigateBack = onNavigateBack,
            onDismissAd = { pagerState.onDismissAd(pageIndex) },
            onImageStateChanged = onImageStateChanged,
            modifier = Modifier
                .fillMaxSize()
                .testTag(PictureDetailsScreenContentImage)
        )
    }
}
