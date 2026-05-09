package com.doodle.core.domain.paginator

import com.doodle.core.domain.source.ImageRepository

sealed class PaginatorRetrievalResult(
    val paginatorWrapper: PaginatorWrapper<out ImageRepository>,
    open val isCached: Boolean
) {
    data class Local(
        val paginator: PaginatorLocalWrapper,
        override val isCached: Boolean
    ) : PaginatorRetrievalResult(paginatorWrapper = paginator, isCached = isCached)

    data class Remote(
        val paginator: PaginatorRemoteWrapper,
        override val isCached: Boolean
    ) : PaginatorRetrievalResult(paginatorWrapper = paginator, isCached = isCached)
}