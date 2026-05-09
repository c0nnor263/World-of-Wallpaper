package com.doodle.core.domain.paginator

import com.doodle.core.domain.model.remote.RemoteImage
import com.doodle.core.domain.source.local.repository.LocalImageRepository
import com.jamal_aliev.paginator.Paginator
import com.jamal_aliev.paginator.dsl.paginator
import com.jamal_aliev.paginator.load.LoadResult
// TODO: Consider adding image request info parameter
class PaginatorLocalWrapper(key: String, source: LocalImageRepository) :
    PaginatorWrapper<LocalImageRepository>(key = key, loadSource = source) {
    override val _paginator: Paginator<RemoteImage.Hit> = paginator {
        load { _ ->
            LoadResult(loadSource.getPagingSource())
        }
    }
}