package com.doodle.core.data.source.remote

import com.doodle.core.data.domain.ApplicationPaginatorDataStore
import com.doodle.core.domain.model.remote.ImageRequestInfo
import com.doodle.core.domain.paginator.PaginatorLocalWrapper
import com.doodle.core.domain.paginator.PaginatorRemoteWrapper
import com.doodle.core.domain.paginator.PaginatorRetrievalResult
import com.doodle.core.domain.paginator.PaginatorWrapper
import com.doodle.core.domain.source.ImageRepository
import com.doodle.core.domain.source.local.repository.LocalImageRepository
import com.doodle.core.domain.source.remote.repository.RemoteImageRepository
import org.koin.core.annotation.Single

@Single(binds = [ApplicationPaginatorDataStore::class])
class ApplicationPaginatorDataStoreImpl : ApplicationPaginatorDataStore {
    override val data: MutableMap<String, PaginatorWrapper<out ImageRepository>> = mutableMapOf()

    override fun getLocalPaginator(
        key: String,
        source: LocalImageRepository
    ): PaginatorRetrievalResult.Local {
        val isCached = key in data
        // TODO: Migrate to PersistentPagingCache
        val paginatorWrapper = data.getOrPut(key) {
            PaginatorLocalWrapper(
                key = key,
                source = source
            )
        }
        return PaginatorRetrievalResult.Local(
            paginator = paginatorWrapper as PaginatorLocalWrapper,
            isCached = isCached
        )
    }

    override fun getRemotePaginator(
        key: String,
        info: ImageRequestInfo,
        source: RemoteImageRepository
    ): PaginatorRetrievalResult.Remote {
        val isCached = key in data
        val paginatorWrapper = data.getOrPut(key) {
            PaginatorRemoteWrapper(
                key = key,
                source = source,
                info = info
            )
        }
        return PaginatorRetrievalResult.Remote(
            paginator = paginatorWrapper as PaginatorRemoteWrapper,
            isCached = isCached
        )
    }
}