package com.doodle.feature.picturedetails.presentation

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.doodle.core.data.domain.ApplicationPaginatorDataStore
import com.doodle.core.database.domain.model.FavoriteImage
import com.doodle.core.database.domain.repository.FavoriteImageRepository
import com.doodle.core.domain.AppLogger
import com.doodle.core.domain.di.IoDispatcher
import com.doodle.core.domain.enums.ActionType
import com.doodle.core.domain.enums.PaginatorKey
import com.doodle.core.domain.model.remote.ImageRequestInfo
import com.doodle.core.domain.paginator.PaginatorRetrievalResult
import com.doodle.core.domain.paginator.PaginatorWrapper
import com.doodle.core.domain.source.ImageRepository
import com.doodle.core.domain.source.local.repository.StorageManager
import com.doodle.core.domain.source.remote.repository.FeedImageRepository
import com.doodle.core.domain.source.remote.repository.SearchImageRepository
import com.doodle.core.navigation.args.PictureDetailsNavArgs
import com.doodle.feature.picturedetails.PICTURE_DETAILS_PREFETCH_DISTANCE
import com.doodle.feature.picturedetails.domain.model.ActionRowClickData
import com.doodle.feature.picturedetails.domain.model.PublisherInfoData
import com.doodle.feature.picturedetails.state.PictureDetailsState
import com.jamal_aliev.paginator.extension.prefetchController
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.SYSTEM
import okio.buffer
import org.jetbrains.compose.resources.getString
import org.koin.core.annotation.KoinViewModel
import worldofwallpapers.feature.picturedetails.generated.resources.Res
import worldofwallpapers.feature.picturedetails.generated.resources.failed_to_save_image
import worldofwallpapers.feature.picturedetails.generated.resources.image_is_not_ready

@KoinViewModel
class PictureDetailsViewModel(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val favoriteImageRepository: FavoriteImageRepository,
    private val storageManager: StorageManager,
    private val searchImageRepository: SearchImageRepository,
    private val feedImageRepository: FeedImageRepository,
    private val applicationPaginatorDataStore: ApplicationPaginatorDataStore,
//    private val nativeAdManager: NativeAdManager,
//    private val appOpenAdManager: AppOpenAdManager
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState?> = MutableStateFlow(UiState.Initializing)
    val uiState = _uiState.asStateFlow()

    val pictureDetailsState = PictureDetailsState()

    lateinit var paginator: PaginatorWrapper<out ImageRepository>

    fun setPagingData(args: PictureDetailsNavArgs) {
        pictureDetailsState.updateData(args.paginatorKey, args.searchQuery)
        AppLogger.i(
            "TAG",
            "Setting paging data for key ${args.paginatorKey}, isLocalSource: ${isLocalSource(args.paginatorKey)}"
        )
        val paginatorReceivingResult = createPaginator(args.paginatorKey)
        paginator = paginatorReceivingResult.paginatorWrapper
        if (!paginatorReceivingResult.isCached) {
            viewModelScope.launch {
                paginator.jumpForward()
                AppLogger.i(
                    "TAG",
                    "Created paginator for key ${args.paginatorKey}, isLocalSource: ${
                        isLocalSource(
                            args.paginatorKey
                        )
                    }, isCached: ${paginatorReceivingResult.isCached}"
                )
            }
        }
        updateUiState(UiState.Ready)
    }

    fun createPaginator(paginatorKey: PaginatorKey): PaginatorRetrievalResult {
        val info = ImageRequestInfo(
            options = ImageRequestInfo.RemoteOption(
                paginatorKey = paginatorKey
            )
        )

        return if (isLocalSource(paginatorKey)) {
            // There is no need to differentiate the source for local data since there is only one local source
            applicationPaginatorDataStore.getLocalPaginator(
                key = paginatorKey.order,
                source = favoriteImageRepository
            )
        } else {
            val source = when (paginatorKey) {
                PaginatorKey.SEARCH -> searchImageRepository
                else -> feedImageRepository
            }
            applicationPaginatorDataStore.getRemotePaginator(
                key = paginatorKey.order,
                info = info,
                source = source
            )
        }.apply {
            paginatorWrapper.raw.prefetchController(
                viewModelScope,
                prefetchDistance = PICTURE_DETAILS_PREFETCH_DISTANCE
            )
        }
    }

    fun isLocalSource(paginatorKey: PaginatorKey): Boolean {
        return paginatorKey == PaginatorKey.FAVORITES
    }

    override fun onCleared() {
        // TODO: Pass the source where it was used instead of clearing all the paginators
//        if (pictureDetailsState.pagingKey == PaginatorKey.FAVORITES) {
//            applicationPaginatorDataStore.clear(
//                key = pictureDetailsState.pagingKey.order
//            )
//        }
        super.onCleared()
    }

    private fun isPictureReadyForActions() =
        uiState.value !is UiState.ImageStateLoading &&
                uiState.value !is UiState.Error

    fun onActionClick(
        platformContext: PlatformContext,
        clickData: ActionRowClickData
    ) = viewModelScope.launch {
        val (type, image, _) = clickData
        // TODO: Think about a better check
        if (!isPictureReadyForActions()) {
            updateUiState(
                UiState.Error(getString(Res.string.image_is_not_ready))
            )
        }
        val imageLoader = SingletonImageLoader.get(platformContext)
        when (type) {
            ActionType.FAVORITE -> processFavoriteAction(
                imageLoader = imageLoader,
                clickData = clickData
            )

            ActionType.DOWNLOAD -> {
                updateUiState(UiState.Actions.StartSavingPictureToDevice)
                val savedUri = saveFavoriteImage(imageLoader, clickData) ?: return@launch
                updateUiState(UiState.Actions.ExportFileToDownloads(savedUri))
            }

            ActionType.SHARE -> {
                val savedImageUri = saveFavoriteImage(imageLoader, clickData) ?: return@launch
                updateUiState(UiState.Actions.Share(savedImageUri))
            }

            ActionType.PUBLISHER_INFO -> {
                val data = PublisherInfoData().createFromImage(image)
                updateUiState(UiState.Actions.ShowPublisherInfo(data))
            }

            ActionType.SET_WALLPAPER -> {
                val savedImageUri = saveFavoriteImage(imageLoader, clickData) ?: return@launch
                updateUiState(UiState.Actions.SetWallpaper(uri = savedImageUri))
            }

            ActionType.NOT_DEFINED -> {}
        }
    }

    private suspend fun processFavoriteAction(
        imageLoader: ImageLoader,
        clickData: ActionRowClickData
    ) {
        val image = clickData.image
        val diskCacheKey = clickData.diskCacheKey
        val favoriteImage = FavoriteImage(
            diskCacheKey = diskCacheKey,
            localUri = "" // Dummy value
        ).copyFromRemoteImageHit(image)

        if (pictureDetailsState.isFavorite) {
            // TODO: Consider saving a preview image and later deleting it instead of deleting the original image
            favoriteImageRepository.deleteById(favoriteImage.imageId)
            paginator.restart()
            if (favoriteImageRepository.getCount() == 0 && isLocalSource(pictureDetailsState.paginatorKey)) {
                updateUiState(UiState.NoMoreFavorites)
            } else {
                updateUiState(UiState.Actions.RemovedPictureFromFavorites)
            }
        } else {
            val savedImageUri = saveFavoriteImage(imageLoader, clickData) ?: return
            val savedFavoriteImage = favoriteImage.copy(
                localUri = savedImageUri
            )

            favoriteImageRepository.upsertById(savedFavoriteImage)
            paginator.restart()
            updateUiState(UiState.Actions.SavedPictureToFavorites)
        }
        pictureDetailsState.isFavorite = !pictureDetailsState.isFavorite
    }

    // TODO: Consider adding a check for existing image before saving to avoid duplicates and unnecessary saving/loading
    private suspend fun saveFavoriteImage(
        imageLoader: ImageLoader,
        clickData: ActionRowClickData
    ): String? = withContext(ioDispatcher) {
        val image = clickData.image
        val diskCacheKey = clickData.diskCacheKey

        val imageBytes = getImageBytes(imageLoader, diskCacheKey)
        val storageInfo = image.createStorageInfo(imageBytes)
        val savedUri: String? = storageManager.saveImage(storageInfo)
        AppLogger.i(
            "TAG",
            "storage info: $storageInfo, savedUri: $savedUri diskCacheKey: $diskCacheKey"
        )
        if (savedUri.isNullOrBlank()) {
            updateUiState(UiState.Error(getString(Res.string.failed_to_save_image)))
            null
        } else {
            savedUri
        }
    }

    private fun getImageBytes(imageLoader: ImageLoader, key: String?): ByteArray? {
        if (key == null) return null
        return imageLoader.diskCache
            ?.openSnapshot(key)
            ?.use { snapshot ->
                FileSystem.SYSTEM
                    .source(snapshot.data)
                    .buffer()
                    .readByteArray()
            }
    }

    fun checkForFavorite(imageId: Int?) {
        pictureDetailsState.isFavorite = false
        viewModelScope.launch(ioDispatcher) {
            pictureDetailsState.isFavorite = imageId?.let {
                favoriteImageRepository.checkForFavorite(it)
            } ?: false
        }
    }

    fun updateUiState(state: UiState?) {
        _uiState.value = state
    }

    fun clearUiState() {
        _uiState.value = null
    }


//    fun getNativeAdById(id: Int): NativeAd? {
//        return if (pictureDetailsState.pagingKey.value != PagingKey.FAVORITES) {
//            nativeAdManager.getNativeAdById(id)
//        } else {
//            null
//        }
//    }

    fun dismissNativeAd(id: Int) {
//        nativeAdManager.dismissAd(id)
    }


//    fun showAppOpenAd(activity: ComponentActivity) {
//        appOpenAdManager.showAdIfAvailable(activity)
//    }

    sealed class UiState {
        data object Initializing : UiState()
        data object Ready : UiState()
        data class Error(val message: String) : UiState()
        data object ImageStateLoaded : UiState()
        data object ImageStateLoading : UiState()
        data object NoMoreFavorites : UiState()

        sealed class Actions {

            @Stable
            data class SetWallpaper(val uri: String) : UiState()

            @Stable
            data class Share(val uri: String) : UiState()
            data class ShowPublisherInfo(val data: PublisherInfoData) : UiState()
            data object SavedPictureToFavorites : UiState()
            data object RemovedPictureFromFavorites : UiState()
            data object StartSavingPictureToDevice : UiState()
            data object FinishedSavingPictureToDevice : UiState()
            data class ExportFileToDownloads(val path: String) : UiState()
        }
    }
}
