package com.doodle.core.network.data.repo

import com.doodle.core.domain.model.remote.RemoteImage
import com.doodle.core.domain.source.remote.repository.TagImageRepository
import com.doodle.core.network.source.PixabayService
import org.koin.core.annotation.Single

@Single(binds = [TagImageRepository::class])
class TagImageRepositoryImpl(
    private val pixabayService: PixabayService
) : TagImageRepository {
    override suspend fun getByTitle(tag: String): RemoteImage.Hit? {
        return try {
            val result = pixabayService.getImagesByPage(q = tag)
            result.hits?.random()
        } catch (e: Exception) {
            null
        }
    }
}
