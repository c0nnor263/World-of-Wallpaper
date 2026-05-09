package com.doodle.feature.home.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.doodle.core.domain.enums.PaginatorKey
import com.doodle.core.domain.paginator.PaginatorRetrievalResult
import com.doodle.feature.home.domain.allTagCategories
import com.doodle.feature.home.domain.model.remote.TagData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

val LocalHomePagingState = compositionLocalOf<HomePagingState> {
    error("HomePagingState was not provided")
}

@Stable
class HomePagingState(
    private val onUpdateTagList: (ImmutableList<TagData>) -> Unit = {},
    val getPaginator: (PaginatorKey) -> PaginatorRetrievalResult
) {
    private val _keyState: MutableStateFlow<PaginatorKey> = MutableStateFlow(PaginatorKey.LATEST)
    val keyState: StateFlow<PaginatorKey> = _keyState.asStateFlow()

    val tagList = mutableStateListOf<TagData>().apply {
        addAll(
            allTagCategories.map { tag ->
                TagData(
                    title = tag,
                    image = mutableStateOf(null)
                )
            }
        )
    }

    var isRetrying by mutableStateOf(false)

    fun updateKey(key: PaginatorKey) {
        _keyState.value = key

        if (key == PaginatorKey.TAGS) {
            val result = tagList.any { it.image.value == null }
            if (result) onUpdateTagList(tagList.toImmutableList())
        }
    }

    fun retry() {
        isRetrying = true
    }

    fun retryComplete() {
        isRetrying = false
    }
}
