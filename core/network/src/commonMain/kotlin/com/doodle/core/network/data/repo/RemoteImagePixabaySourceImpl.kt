package com.doodle.core.network.data.repo

import com.doodle.core.domain.model.remote.ImageRequestInfo
import com.doodle.core.domain.model.remote.RemoteImage
import com.doodle.core.domain.source.remote.RemoteImagePixabaySource
import com.doodle.core.network.source.PixabayService
import org.koin.core.annotation.Single

@Single(binds = [RemoteImagePixabaySource::class])
class RemoteImagePixabaySourceImpl(
    private val pixabayService: PixabayService
) : RemoteImagePixabaySource {
    override suspend fun getImagesByPage(info: ImageRequestInfo): RemoteImage {
        return pixabayService.getImagesByPage(
            pageKey = info.pageKey,
            q = info.options.query,
            category = info.options.category,
            order = info.options.paginatorKey.order,
            perPage = info.pageSize,
            editorsChoice = info.options.isPremium
        )
    }
}
