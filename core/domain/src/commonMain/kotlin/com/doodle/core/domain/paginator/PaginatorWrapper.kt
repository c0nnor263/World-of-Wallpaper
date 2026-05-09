package com.doodle.core.domain.paginator

import com.doodle.core.domain.model.remote.RemoteImage
import com.doodle.core.domain.source.ImageRepository
import com.jamal_aliev.paginator.Paginator
import com.jamal_aliev.paginator.extension.uiState
import com.jamal_aliev.paginator.page.PaginatorUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Base wrapper around a concrete paginator implementation used for loading paginated image data.
 *
 * This class hides the external paginator API behind application-specific abstractions:
 * - exposes [PaginatorWrapperUiState] instead of the library-specific [PaginatorUiState];
 * - keeps the current image source used for loading pages;
 * - provides a small set of pagination actions required by the UI layer.
 *
 * The wrapper is intended to make the rest of the app depend on a stable internal contract
 * rather than on a concrete third-party paginator implementation. If the paginator library
 * changes in the future, only this layer and its subclasses should need to be updated.
 *
 * Subclasses are responsible for creating the actual [_paginator] instance and defining how
 * pages are loaded from [loadSource].
 *
 * @param T The type of image repository/source used by this paginator.
 * @property key A unique key identifying this paginator instance.
 * @property loadSource The current repository/source used to load paginated data.
 */
abstract class PaginatorWrapper<T : ImageRepository>(
    val key: String,
    protected var loadSource: T
) {
    protected abstract val _paginator: Paginator<RemoteImage.Hit>

    /**
     * Exposes the underlying paginator instance for direct access when needed, but encourages using the wrapper's own API and UI state instead
     */
    val raw: Paginator<RemoteImage.Hit>
        get() = _paginator
    val uiState: Flow<PaginatorWrapperUiState<RemoteImage.Hit>> get() =
        mapToWrapperUiState(_paginator.uiState)

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun mapToWrapperUiState(
        paginatorUiState: Flow<PaginatorUiState<RemoteImage.Hit>>,
    ): Flow<PaginatorWrapperUiState<RemoteImage.Hit>> {
        return paginatorUiState.map { state ->
            when (state) {
                is PaginatorUiState.Idle -> PaginatorWrapperUiState.Idle
                is PaginatorUiState.Loading -> PaginatorWrapperUiState.Loading(page = state.page)
                is PaginatorUiState.Empty -> PaginatorWrapperUiState.Empty(page = state.page)
                is PaginatorUiState.Error -> PaginatorWrapperUiState.Error(
                    page = state.page,
                    exception = state.exception,
                )

                is PaginatorUiState.Content -> PaginatorWrapperUiState.Content(
                    items = state.items,
                )
            }
        }
    }

    suspend fun updateSource(newSource: T) {
        loadSource = newSource
        restart()
    }

    suspend fun restart() {
        _paginator.restart()
    }

    fun release() {
        _paginator.release()
    }

    suspend fun goNextPage() {
        _paginator.goNextPage()
    }

    suspend fun jumpForward() {
        _paginator.jumpForward()
    }
}