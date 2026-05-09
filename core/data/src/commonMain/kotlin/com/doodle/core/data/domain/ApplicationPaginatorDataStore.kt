package com.doodle.core.data.domain

import com.doodle.core.domain.model.remote.ImageRequestInfo
import com.doodle.core.domain.paginator.PaginatorRetrievalResult
import com.doodle.core.domain.paginator.PaginatorWrapper
import com.doodle.core.domain.source.ImageRepository
import com.doodle.core.domain.source.local.repository.LocalImageRepository
import com.doodle.core.domain.source.remote.repository.RemoteImageRepository

interface ApplicationPaginatorDataStore {
    val data: MutableMap<String, PaginatorWrapper<out ImageRepository>>

    fun getLocalPaginator(
        key: String,
        source: LocalImageRepository
    ): PaginatorRetrievalResult.Local

    fun getRemotePaginator(
        key: String,
        info: ImageRequestInfo,
        source: RemoteImageRepository
    ): PaginatorRetrievalResult.Remote

    fun onCleared() {
        data.values.forEach { it.release() }
        data.clear()
    }

    fun clear(key: String) {
        data[key]?.release()
        data.remove(key)
    }
}
