package com.doodle.core.domain.paginator

import com.doodle.core.domain.model.remote.ImageRequestInfo
import com.doodle.core.domain.model.remote.RemoteImage
import com.doodle.core.domain.source.remote.repository.RemoteImageRepository
import com.jamal_aliev.paginator.Paginator
import com.jamal_aliev.paginator.dsl.paginator
import com.jamal_aliev.paginator.load.LoadResult

class PaginatorRemoteWrapper(
    key: String,
    source: RemoteImageRepository,
    var info: ImageRequestInfo,
) :
    PaginatorWrapper<RemoteImageRepository>(key = key, loadSource = source) {
    override val _paginator: Paginator<RemoteImage.Hit> = paginator(capacity = info.pageSize) {
        load { page ->
            LoadResult(
                loadSource.getPagingSource(
                    info.copy(pageKey = page)
                )
            )
        }
    }

    suspend fun updateInfo(newInfo: ImageRequestInfo) {
        info = newInfo
        _paginator.restart()
    }
}