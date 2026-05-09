package com.doodle.feature.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doodle.core.data.domain.ApplicationPaginatorDataStore
import com.doodle.core.database.domain.repository.FavoriteImageRepository
import com.doodle.core.domain.di.IoDispatcher
import com.doodle.core.domain.enums.PaginatorKey
import com.doodle.core.domain.paginator.PaginatorRetrievalResult
import com.doodle.core.domain.source.local.repository.StorageManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class FavoritesScreenViewModel(
    private val favoriteImageRepository: FavoriteImageRepository,
    private val applicationPaginatorDataStore: ApplicationPaginatorDataStore,
    private val storageManager: StorageManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    val paginator = createPaginator().paginatorWrapper

    fun createPaginator(): PaginatorRetrievalResult {
        return applicationPaginatorDataStore.getLocalPaginator(
            key = PaginatorKey.FAVORITES.order,
            source = favoriteImageRepository
        )
    }

    init {
        viewModelScope.launch {
            paginator.jumpForward()
        }
    }

    override fun onCleared() {
        applicationPaginatorDataStore.clear(
            PaginatorKey.FAVORITES.order
        )
        super.onCleared()
    }

    // TODO: Revisit this logic. There is actually no need to delete the item from the database if the file is not exists.
    //  Consider using Coil cache to check if the image is exists or not
    fun onCheckImageExisting() = viewModelScope.launch(ioDispatcher) {
//        val contentItems =
//            (paginator.uiState.firstOrNull() as? PaginatorWrapperUiState.Content)?.items
//        if (contentItems.isNullOrEmpty()) return@launch
//
//        var isContentItemsChanged = false
//        contentItems.forEach { image ->
//            val imageInfo = image.createStorageInfo(null)
//            val id = imageInfo.id
//            val isExists = storageManager.isFileExists(imageInfo)
//            AppLogger.i("TAG", "Image with id $id is exists: $isExists")
//            if (!isExists && id != null) {
//                favoriteImageRepository.deleteById(id)
//                isContentItemsChanged = true
//            }
//        }
//        if (isContentItemsChanged) {
//            paginator.restart()
//        }
    }
}
