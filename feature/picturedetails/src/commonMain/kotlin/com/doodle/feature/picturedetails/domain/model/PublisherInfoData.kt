package com.doodle.feature.picturedetails.domain.model

import com.doodle.core.domain.model.remote.RemoteImage

data class PublisherInfoData(
    val comments: Int? = null,
    val downloads: Int? = null,
    val imageHeight: Int? = null,
    val imageWidth: Int? = null,
    val largeImageURL: String? = null,
    val likes: Int? = null,
    val pageURL: String? = null,
    val previewURL: String? = null,
    val tags: String? = null,
    val type: String? = null,
    val user: String? = null,
    val userImageURL: String? = null,
    val userId: Int? = null,
    val views: Int? = null
) {
    fun createFromImage(image: RemoteImage.Hit?): PublisherInfoData {
        return PublisherInfoData(
            comments = image?.comments,
            downloads = image?.downloads,
            imageHeight = image?.imageHeight,
            imageWidth = image?.imageWidth,
            largeImageURL = image?.largeImageURL,
            likes = image?.likes,
            pageURL = image?.pageURL,
            previewURL = image?.previewURL,
            tags = image?.tags,
            type = image?.type,
            user = image?.user,
            userImageURL = image?.userImageURL,
            userId = image?.user_id,
            views = image?.views
        )
    }
}
