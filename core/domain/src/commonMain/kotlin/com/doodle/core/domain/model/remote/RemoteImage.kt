package com.doodle.core.domain.model.remote

import com.doodle.core.domain.model.local.StorageImageInfo
import kotlinx.serialization.Serializable

@Serializable
data class RemoteImage(
    val hits: List<Hit>? = null,
    val total: Int? = null,
    val totalHits: Int? = null
) {
    @Serializable
    data class Hit(
        val comments: Int? = null,
        val downloads: Int? = null,
        val fullHDURL: String? = null,
        val id: Int? = null,
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
        fun createStorageInfo(byteArray: ByteArray?): StorageImageInfo {
            return StorageImageInfo(
                id = id,
                userId = user_id,
                mimeType = type,
                byteArray = byteArray
            )
        }
    }
}
