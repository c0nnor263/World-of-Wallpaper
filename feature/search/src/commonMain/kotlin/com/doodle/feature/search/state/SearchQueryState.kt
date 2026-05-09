package com.doodle.feature.search.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Stable
class SearchQueryState {
    private val _query: MutableStateFlow<String> = MutableStateFlow("")
    val query: StateFlow<String> = _query
    var isFocused by mutableStateOf(false)
        private set
    var isSearching by mutableStateOf(false)

    var isRetrying by mutableStateOf(false)
        private set

    fun updateQuery(data: String) {
        _query.value = data
    }

    fun retry() {
        isRetrying = true
    }

    fun retryComplete() {
        isRetrying = false
    }

    fun setFocus() {
        isFocused = true
    }

    fun clearFocus() {
        isFocused = false
    }
}
