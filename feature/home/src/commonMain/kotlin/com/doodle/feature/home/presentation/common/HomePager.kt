package com.doodle.feature.home.presentation.common

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.doodle.core.domain.enums.PaginatorKey
import com.doodle.core.domain.model.remote.RemoteImage
import com.doodle.core.domain.paginator.PaginatorWrapperUiState
import com.doodle.core.navigation.args.PictureDetailsNavArgs
import com.doodle.core.navigation.args.SearchNavArgs
import com.doodle.core.ui.FetchedImageItem
import com.doodle.core.ui.card.CardImageList
import com.doodle.core.ui.card.EmptyListContent
import com.doodle.feature.home.domain.enums.TabPage
import com.doodle.feature.home.domain.model.remote.TagData
import com.doodle.feature.home.offsetForPage
import com.doodle.feature.home.presentation.HomeScreenViewModel
import com.doodle.feature.home.state.LocalHomePagingState
import com.jamal_aliev.paginator.compose.paginated
import com.jamal_aliev.paginator.compose.rememberPaginated
import com.jamal_aliev.paginator.page.PaginatorUiState
import org.jetbrains.compose.resources.stringResource
import worldofwallpapers.core.ui.generated.resources.no_images_available
import worldofwallpapers.core.ui.generated.resources.no_internet_connection
import kotlin.math.absoluteValue
import kotlin.math.min

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onNavigateToDetails: (PictureDetailsNavArgs) -> Unit,
    onNavigateToSearch: (SearchNavArgs) -> Unit,
    onUpdateUiState: (HomeScreenViewModel.UiState?) -> Unit
) {
    val homePagingState = LocalHomePagingState.current

    LaunchedEffect(pagerState.currentPage) {
        val pagingKey = TabPage.entries[pagerState.currentPage].paginatorKey
        homePagingState.updateKey(pagingKey)
    }

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        key = { index ->
            TabPage.entries[index].labelResource.key
        }
    ) { pageIndex ->
        PagerContent(
            pagerState = pagerState,
            pageIndex = pageIndex,
            onNavigateToDetails = onNavigateToDetails,
            onNavigateToSearch = onNavigateToSearch,
            onUpdateUiState = onUpdateUiState
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerContent(
    pagerState: PagerState,
    pageIndex: Int,
    onNavigateToDetails: (PictureDetailsNavArgs) -> Unit,
    onNavigateToSearch: (SearchNavArgs) -> Unit,
    onUpdateUiState: (HomeScreenViewModel.UiState?) -> Unit
) {
    val homePagingState = LocalHomePagingState.current
    val staggeredGridListState = rememberLazyStaggeredGridState()
    val paginatorKey = TabPage.entries[pageIndex].paginatorKey
    val paginatorReceivingResult = remember(paginatorKey) {
        homePagingState.getPaginator.invoke(paginatorKey)
    }
    val paginator = paginatorReceivingResult.paginatorWrapper

    LaunchedEffect(paginatorKey) {
        if (!paginatorReceivingResult.isCached) {
            paginator.jumpForward()
        }
    }

    val pagingUiState = paginator.uiState.collectAsStateWithLifecycle(null).value
    val paged = paginator.raw.rememberPaginated(state = staggeredGridListState)

    LaunchedEffect(homePagingState.isRetrying) {
        if (homePagingState.isRetrying) {
            paginator.restart()
            homePagingState.retryComplete()
            onUpdateUiState(HomeScreenViewModel.UiState.Loading)
        }
    }

    when (pagingUiState) {
        PaginatorUiState.Idle -> onUpdateUiState(null)
        is PaginatorWrapperUiState.Loading -> {
            val state = HomeScreenViewModel.UiState.Loading
            onUpdateUiState(state)
            // TODO:
        }

        is PaginatorWrapperUiState.Empty -> {
            // TODO:
        }

        is PaginatorWrapperUiState.Error -> {
            val msg = stringResource(worldofwallpapers.core.ui.generated.resources.Res.string.no_internet_connection)
            val state =
                HomeScreenViewModel.UiState.Error(message = pagingUiState.exception.message ?: msg)
            onUpdateUiState(state)
        }

        is PaginatorWrapperUiState.Content -> {
            // TODO:
            onUpdateUiState(null)
        }

        else -> {}
    }

    val imageCount =
        if (paginatorKey == PaginatorKey.TAGS) {
            homePagingState.tagList.size
        } else {
            if(pagingUiState is PaginatorUiState.Empty) 0 else (pagingUiState as? PaginatorWrapperUiState.Content)?.items?.count() ?: 0
        }
    CardImageList(
        state = staggeredGridListState,
        modifier = Modifier.graphicsLayer {
            val pageOffset = pagerState.offsetForPage(pageIndex)
            val interpolated = FastOutLinearInEasing.transform(pageOffset.absoluteValue)
            val interpolatedScale = 1F - interpolated * 0.2f
            scaleX = min(1f, interpolatedScale)
            scaleY = min(1f, interpolatedScale)
        },
        columns = StaggeredGridCells.Fixed(if (paginatorKey == PaginatorKey.TAGS) 2 else 3),
        isItemsEmpty = imageCount == 0,
        onEmptyContent = {
            EmptyListContent(
                textPlaceholder = stringResource(
                    worldofwallpapers.core.ui.generated.resources.Res.string.no_images_available
                )
            )
        }
    ) {
        paginated(paged) {
            when (paginatorKey) {
                PaginatorKey.TAGS -> {
                    TagList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        items = homePagingState.tagList,
                        onNavigateToSearch = {
                            val args = SearchNavArgs(it)
                            onNavigateToSearch(args)
                        }
                    )
                }

                else -> {
                    val isPremium = paginatorKey == PaginatorKey.EDITORS_CHOICE
                    val items = (pagingUiState as? PaginatorWrapperUiState.Content)?.items ?: emptyList()
                    ImageList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        items = items,
                        isPremium = isPremium,
                        onNavigateToDetails = { index ->
                            val args =
                                PictureDetailsNavArgs(
                                    index,
                                    paginatorKey,
                                    isPremium = isPremium
                                )
                            if (!isPremium) {
                                onNavigateToDetails(args)
                            } else {
                                onUpdateUiState(HomeScreenViewModel.UiState.Premium(args))
                            }
                        }
                    )
                }
            }
        }
    }
}

fun LazyStaggeredGridScope.TagList(
    items: SnapshotStateList<TagData>,
    onNavigateToSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    items(items, key = { it.title }) { (title, image) ->
        TagCard(
            title = title,
            previewURL = image.value?.largeImageURL ?: "",
            onNavigateToSearch = onNavigateToSearch,
            modifier = modifier
        )
    }
}

fun LazyStaggeredGridScope.ImageList(
    modifier: Modifier = Modifier,
    isPremium: Boolean,
    items: List<RemoteImage.Hit>,
    onNavigateToDetails: (Int) -> Unit
) {
    items(items.count()) { index ->
        val image = items[index]
        FetchedImageItem(
            previewURL = image.previewURL ?: "",
            onNavigateToDetails = { onNavigateToDetails(index) },
            isPremium = isPremium,
            modifier = modifier
        )
    }
}
