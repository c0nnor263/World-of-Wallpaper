package com.doodle.core.data.source.local.paging
//
//import android.util.Log
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.doodle.core.database.dao.FavoriteImageDao
//import com.doodle.core.domain.model.remote.RemoteImage
//
//class FavoriteImagePagingSource(
//    private val favoriteImageDao: FavoriteImageDao
//) : PagingSource<Int, RemoteImage.Hit>() {
//    override fun getRefreshKey(state: PagingState<Int, RemoteImage.Hit>): Int? {
//        return state.anchorPosition
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RemoteImage.Hit> {
//        return try {
//            val key = params.key ?: 1
//            val favoriteImages = favoriteImageDao.getAll()
//            Log.i("TAG", "load: Favorite $key")
//            val fetchedImageHit = favoriteImages.map {
//                it.mapToFetchedImageHit()
//            }
//
//            LoadResult.Page(
//                data = fetchedImageHit,
//                prevKey = if (key == 1) null else key - 1,
//                nextKey = if (favoriteImages.isEmpty()) null else key + 1
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//}
