package com.doodle.core.domain.source.local.repository

import com.doodle.core.domain.model.remote.RemoteImage
import com.doodle.core.domain.source.ImageRepository

interface LocalImageRepository: ImageRepository {
    suspend fun getPagingSource(): List<RemoteImage.Hit>
}