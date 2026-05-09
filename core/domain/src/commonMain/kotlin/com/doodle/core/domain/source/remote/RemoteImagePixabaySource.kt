package com.doodle.core.domain.source.remote

import com.doodle.core.domain.model.remote.ImageRequestInfo
import com.doodle.core.domain.model.remote.RemoteImage

interface RemoteImagePixabaySource {
    suspend fun getImagesByPage(info: ImageRequestInfo): RemoteImage
}
