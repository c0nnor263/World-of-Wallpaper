package com.doodle.core.domain.source.remote.repository

import com.doodle.core.domain.model.remote.RemoteImage

fun interface TagImageRepository {
    suspend fun getByTitle(tag: String): RemoteImage.Hit?
}
