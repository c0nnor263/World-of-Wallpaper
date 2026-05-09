package com.doodle.core.data.source.remote.repository

import com.doodle.core.domain.model.remote.ImageRequestInfo
import com.doodle.core.domain.model.remote.RemoteImage
import com.doodle.core.domain.source.remote.RemoteImagePixabaySource
import com.doodle.core.domain.source.remote.repository.FeedImageRepository
import org.koin.core.annotation.Single

@Single(binds = [FeedImageRepository::class])
class FeedImageRepositoryImpl(
    private val remoteImagePixabaySource: RemoteImagePixabaySource
) : FeedImageRepository {
    override suspend fun getPagingSource(info: ImageRequestInfo): List<RemoteImage.Hit> {
        val response = remoteImagePixabaySource.getImagesByPage(info)
        return response.hits?.run {
            if (info.options.isPremium) {
                shuffled()
            } else this
        } ?: throw Exception("No data")
    }
}
