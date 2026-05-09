package com.doodle.core.domain.model.remote

import com.doodle.core.domain.MAX_PAGE_SIZE
import com.doodle.core.domain.PREFETCH_DISTANCE
import com.doodle.core.domain.enums.PaginatorKey

data class ImageRequestInfo(
    val pageKey: Int = 1,
    val pageSize: Int = MAX_PAGE_SIZE,
    val prefetchDistance: Int = PREFETCH_DISTANCE,
    val options: RemoteOption = RemoteOption(),
) {
    data class RemoteOption(
        val paginatorKey: PaginatorKey = PaginatorKey.LATEST,
        val query: String? = "",
        val category: String = "",
        val isPremium: Boolean = false
    )
}
