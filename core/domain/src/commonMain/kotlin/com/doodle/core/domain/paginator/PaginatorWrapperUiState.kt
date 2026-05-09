package com.doodle.core.domain.paginator

/**
 * Application-level UI state for paginated content.
 *
 * This wrapper represents the pagination state exposed to the UI layer without
 * leaking the concrete paginator implementation or its external API types.
 *
 * Use this state instead of depending directly on a third-party paginator UI state.
 * It allows the app to keep a stable presentation contract even if the underlying
 * pagination library or implementation changes.
 *
 * Typical state flow:
 * 1. [Idle] — pagination has not started yet.
 * 2. [Loading] — the requested page is currently being loaded.
 * 3. [Empty] — the requested page was loaded successfully but returned no items.
 * 4. [Error] — the requested page failed to load.
 * 5. [Content] — paginated items are available for rendering.
 *
 * Unlike more detailed paginator states, this wrapper intentionally exposes only
 * the information required by the app UI. Boundary pagination states such as
 * "loading next page" or "failed to append page" may be flattened or handled
 * separately by the mapper/adapter layer.
 *
 * @param T The type of item displayed by the paginated UI.
 */
sealed interface PaginatorWrapperUiState<out T> {

    data object Idle : PaginatorWrapperUiState<Nothing>
    data class Loading(val page: Int) : PaginatorWrapperUiState<Nothing>
    data class Empty(val page: Int) : PaginatorWrapperUiState<Nothing>
    data class Error(
        val page: Int,
        val exception: Exception,
    ) : PaginatorWrapperUiState<Nothing>

    data class Content<T>(
        val items: List<T>,
    ) : PaginatorWrapperUiState<T>
}