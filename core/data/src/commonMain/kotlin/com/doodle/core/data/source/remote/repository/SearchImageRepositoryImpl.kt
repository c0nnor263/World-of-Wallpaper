package com.doodle.core.data.source.remote.repository

import com.doodle.core.domain.model.remote.ImageRequestInfo
import com.doodle.core.domain.model.remote.RemoteImage
import com.doodle.core.domain.source.remote.RemoteImagePixabaySource
import com.doodle.core.domain.source.remote.repository.SearchImageRepository
import org.koin.core.annotation.Single

@Single(binds = [SearchImageRepository::class])
class SearchImageRepositoryImpl(
    private val remoteImagePixabaySource: RemoteImagePixabaySource
) : SearchImageRepository {
    override suspend fun getPagingSource(info: ImageRequestInfo): List<RemoteImage.Hit> {
        val response = remoteImagePixabaySource.getImagesByPage(info)
        return response.hits ?: throw Exception("No data")
    }
}
