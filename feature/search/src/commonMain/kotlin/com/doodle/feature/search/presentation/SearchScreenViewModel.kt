package com.doodle.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doodle.core.data.domain.ApplicationPaginatorDataStore
import com.doodle.core.domain.enums.PaginatorKey
import com.doodle.core.domain.model.remote.ImageRequestInfo
import com.doodle.core.domain.paginator.PaginatorRetrievalResult
import com.doodle.core.domain.source.remote.repository.SearchImageRepository
import com.doodle.core.navigation.args.SearchNavArgs
import com.doodle.feature.search.state.SearchQueryState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.KoinViewModel
import kotlin.time.Duration.Companion.milliseconds

@KoinViewModel
class SearchScreenViewModel(
    private val applicationPaginatorDataStore: ApplicationPaginatorDataStore,
    private val searchImageRepository: SearchImageRepository,
//    private val appOpenAdManager: AppOpenAdManager
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState?>(UiState.Loading)
    val uiState = _uiState.asStateFlow()


    val searchQueryState = SearchQueryState()

    val paginator = createPaginator().paginator

    private fun createPaginator(): PaginatorRetrievalResult.Remote {
        val info = ImageRequestInfo(
            options = ImageRequestInfo.RemoteOption(
                query = searchQueryState.query.value,
            )
        )

        return applicationPaginatorDataStore.getRemotePaginator(
            key = PaginatorKey.SEARCH.order,
            info = info,
            source = searchImageRepository
        )
    }

    init {
        observeSearchQuery()
    }

    override fun onCleared() {
        applicationPaginatorDataStore.clear(
            key = PaginatorKey.SEARCH.order
        )
        super.onCleared()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        searchQueryState.query
            .debounce(500.milliseconds)
            .onEach { query ->
                searchQueryState.isSearching = true
                val info = ImageRequestInfo(
                    options = ImageRequestInfo.RemoteOption(
                        query = query,
                    )
                )
                paginator.updateInfo(info)
                searchQueryState.isSearching = false
            }
            .launchIn(viewModelScope)
    }

    fun setSearchState(navArgs: SearchNavArgs) {
        if (searchQueryState.query.value == navArgs.query || navArgs.query.isBlank()) return
        searchQueryState.updateQuery(navArgs.query)
    }

    fun updateUiState(uiState: UiState?) {
        _uiState.value = uiState
    }

//    fun showAppOpenAd(activity: ComponentActivity) {
//        appOpenAdManager.showAdIfAvailable(activity)
//    }


    sealed class UiState {
        data object Loading : UiState()
        data class Error(val message: String) : UiState()
    }
}
