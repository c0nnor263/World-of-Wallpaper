package com.doodle.core.domain.source.remote.repository

import com.doodle.core.domain.model.remote.ImageRequestInfo
import com.doodle.core.domain.model.remote.RemoteImage
import com.doodle.core.domain.source.ImageRepository

interface RemoteImageRepository: ImageRepository{
    suspend fun getPagingSource(info: ImageRequestInfo): List<RemoteImage.Hit>
}