package com.doodle.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doodle.core.data.domain.ApplicationPaginatorDataStore
import com.doodle.core.domain.di.IoDispatcher
import com.doodle.core.domain.enums.PaginatorKey
import com.doodle.core.domain.model.remote.ImageRequestInfo
import com.doodle.core.domain.paginator.PaginatorRetrievalResult
import com.doodle.core.domain.source.remote.repository.FeedImageRepository
import com.doodle.core.domain.source.remote.repository.TagImageRepository
import com.doodle.core.navigation.args.PictureDetailsNavArgs
import com.doodle.feature.home.domain.model.remote.TagData
import com.doodle.feature.home.state.HomePagingState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HomeScreenViewModel(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val applicationPaginatorDataStore: ApplicationPaginatorDataStore,
    private val feedImageRepository: FeedImageRepository,
    private val tagImageRepository: TagImageRepository,
//    private val applicationReviewManager: ApplicationReviewManager,
//    private val billingDataSource: BillingDataSource,
//    private val appOpenAdManager: AppOpenAdManager
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState?>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    val homePagingState = HomePagingState(
        onUpdateTagList = {
            getDataForTagList(it)
        },
        // TODO: Pass down to the screen
        getPaginator = ::getPaginator
    )

    fun getPaginator(order: PaginatorKey): PaginatorRetrievalResult {
        return createPaginator(order)
    }

    private fun createPaginator(order: PaginatorKey): PaginatorRetrievalResult {
        val editorsChoice = order == PaginatorKey.EDITORS_CHOICE
        val info = ImageRequestInfo(
            options = ImageRequestInfo.RemoteOption(
                if (editorsChoice) PaginatorKey.LATEST else order,
                isPremium = editorsChoice
            )
        )

        return applicationPaginatorDataStore.getRemotePaginator(
            key = order.order,
            info = info,
            source = feedImageRepository
        )
    }

//    override fun onCleared() {
//        // TODO: Pass the source where it was used instead of clearing all the paginators
//        applicationPaginatorDataStore.onCleared()
//        super.onCleared()
//    }

    fun updateUiState(uiState: UiState?) {
        _uiState.value = uiState
    }

    private fun getDataForTagList(list: ImmutableList<TagData>) =
        viewModelScope.launch(ioDispatcher) {
            updateUiState(UiState.Loading)
            val asyncList = mutableListOf<Deferred<Unit>>()
            list.forEach { tag ->
                asyncList.add(
                    async {
                        tag.image.value = tagImageRepository.getByTitle(tag.title)
                    }
                )
            }
            asyncList.awaitAll()
            updateUiState(null)
        }

//    fun requestApplicationReview(activity: ComponentActivity) = viewModelScope.launch {
//        applicationReviewManager.requestInfo(activity)
//    }

//    fun requestBillingRemoveAds(
//        activity: ComponentActivity,
//        onError: () -> Unit
//    ) = viewModelScope.launch {
//        billingDataSource.purchaseProduct(
//            BillingProductType.REMOVE_ADS,
//            activity = activity,
//            onError = onError
//        )
//    }

//    fun showAppOpenAd(activity: ComponentActivity) {
//        appOpenAdManager.showAdIfAvailable(activity)
//    }

    fun restorePurchases() {
//        billingDataSource.restorePurchases()
    }

    sealed class UiState {
        data class Premium(val args: PictureDetailsNavArgs) : UiState()
        data object Loading : UiState()
        data class Error(val message: String) : UiState()
    }
}
