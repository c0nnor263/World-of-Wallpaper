package com.doodle.feature.picturedetails.state

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.doodle.core.domain.AppLogger
import com.doodle.core.domain.PREFETCH_DISTANCE
import com.doodle.core.domain.enums.RemoveAdsStatus
import com.doodle.core.domain.model.remote.RemoteImage
import com.doodle.core.domain.paginator.PaginatorWrapper
import com.doodle.core.domain.paginator.PaginatorWrapperUiState
import com.doodle.feature.picturedetails.domain.model.PageData
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberPagerDetailState(
    initialPage: Int,
    isScrollEnabled: Boolean,
    paginator: PaginatorWrapper<*>,
//    onGetNativeAd: (Int) -> NativeAd?,
    onDismissAd: (Int) -> Unit,
    onCheckFavorite: (Int?) -> Unit
): PagerPictureDetailState {
    val pagingUiState = paginator.uiState.collectAsStateWithLifecycle(null).value
    val items = (pagingUiState as? PaginatorWrapperUiState.Content)?.items ?: emptyList()
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = {
            items.count()
        }
    )
    AppLogger.i("TAG", "PagerDetailState: items count: ${items.count()}, initialPage: $initialPage")
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState, items.size) {
        snapshotFlow {
            val layoutInfo = pagerState.layoutInfo
            val lastVisibleIndex = layoutInfo.visiblePagesInfo.lastOrNull()?.index ?: -1
            lastVisibleIndex to items.size
        }
            .distinctUntilChanged()
            .collect { (lastVisibleIndex, totalDataItems) ->
                if (totalDataItems > 0 && lastVisibleIndex >= 0 && lastVisibleIndex >= totalDataItems - PREFETCH_DISTANCE) {
                    coroutineScope.launch {
                        paginator.goNextPage()
                    }
                }
            }
    }



    LaunchedEffect(pagerState.currentPage, Unit, items.count()) {
        if (items.count() > 0) {
            val imageId = items.getOrNull(pagerState.currentPage)?.id
            onCheckFavorite(imageId)
        }
    }

    return remember(items.count()) {
        PagerPictureDetailState(
            pagerState = pagerState,
            items = items,
            isScrollEnabled = isScrollEnabled,
//            onGetNativeAd = onGetNativeAd,
            onDismissNativeAd = onDismissAd
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
class PagerPictureDetailState(
    isScrollEnabled: Boolean,
    val pagerState: PagerState,
    val items: List<RemoteImage.Hit>,
//    private val onGetNativeAd: (Int) -> NativeAd?,
    private val onDismissNativeAd: (Int) -> Unit
) {
    val isScrollEnabled by mutableStateOf(isScrollEnabled)

    fun getKey(id: Int): Int {
        return items.getOrNull(id)?.id ?: 0
    }

    fun getPageData(id: Int, removeAdsStatus: RemoveAdsStatus): PageData {
//        val nativeAd = if (removeAdsStatus.isNotPurchased()) onGetNativeAd(id) else null
        return PageData(
            image = mutableStateOf(items[id]),
//            nativeAd = mutableStateOf(nativeAd)
        )
    }

    fun onDismissAd(id: Int) {
        onDismissNativeAd(id)
    }

    fun isActiveNow(pageIndex: Int): Boolean {
        val settledPage = pagerState.settledPage
        val limitToCurrentPage = pagerState.currentPage + 1
        return settledPage.coerceAtMost(limitToCurrentPage) == pageIndex
    }
}
