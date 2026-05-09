package com.doodle.core.database.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.doodle.core.domain.model.remote.RemoteImage

@Entity
data class FavoriteImage(
    @PrimaryKey
    val imageId: Int = 0,

    // Uri to the file on the device
    val localUri: String,
    val diskCacheKey: String?,

    val comments: Int? = null,
    val downloads: Int? = null,
    val fullHDURL: String? = null,
    val imageHeight: Int? = null,
    val imageSize: Int? = null,
    val imageURL: String? = null,
    val imageWidth: Int? = null,
    val largeImageURL: String? = null,
    val likes: Int? = null,
    val pageURL: String? = null,
    val previewHeight: Int? = null,
    val previewURL: String? = null,
    val previewWidth: Int? = null,
    val tags: String? = null,
    val type: String? = null,
    val user: String? = null,
    val userImageURL: String? = null,
    val user_id: Int? = null,
    val views: Int? = null,
    val webformatHeight: Int? = null,
    val webformatURL: String? = null,
    val webformatWidth: Int? = null
) {

    fun mapToRemoteImageHit(): RemoteImage.Hit {
        return RemoteImage.Hit(
            id = imageId,
            comments = comments,
            downloads = downloads,
            fullHDURL = fullHDURL,
            imageHeight = imageHeight,
            imageSize = imageSize,
            imageURL = imageURL,
            imageWidth = imageWidth,
            largeImageURL = localUri,
            likes = likes,
            pageURL = pageURL,
            previewHeight = previewHeight,
            previewURL = previewURL,
            previewWidth = previewWidth,
            tags = tags,
            type = type,
            user = user,
            userImageURL = userImageURL,
            user_id = user_id,
            views = views,
            webformatHeight = webformatHeight,
            webformatURL = webformatURL,
            webformatWidth = webformatWidth
        )
    }

    fun copyFromRemoteImageHit(hit: RemoteImage.Hit): FavoriteImage {
        return this.copy(
            imageId = hit.id ?: 0,
            comments = hit.comments,
            downloads = hit.downloads,
            fullHDURL = hit.fullHDURL,
            imageHeight = hit.imageHeight,
            imageSize = hit.imageSize,
            imageURL = hit.imageURL,
            imageWidth = hit.imageWidth,
            largeImageURL = hit.largeImageURL,
            likes = hit.likes,
            pageURL = hit.pageURL,
            previewHeight = hit.previewHeight,
            previewURL = hit.previewURL,
            previewWidth = hit.previewWidth,
            tags = hit.tags,
            type = hit.type,
            user = hit.user,
            userImageURL = hit.userImageURL,
            user_id = hit.user_id,
            views = hit.views,
            webformatHeight = hit.webformatHeight,
            webformatURL = hit.webformatURL,
            webformatWidth = hit.webformatWidth
        )
    }
}
