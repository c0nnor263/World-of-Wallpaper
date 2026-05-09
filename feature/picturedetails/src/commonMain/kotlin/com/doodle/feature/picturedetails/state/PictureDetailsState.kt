package com.doodle.feature.picturedetails.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.doodle.core.domain.enums.PaginatorKey

@Stable
class PictureDetailsState {
    var paginatorKey: PaginatorKey by mutableStateOf(PaginatorKey.LATEST)

    var isFavorite by mutableStateOf(false)
    var searchQuery by mutableStateOf("")

    fun updateData(key: PaginatorKey, searchQuery: String) {
        paginatorKey = key
        this.searchQuery = searchQuery
    }
}
