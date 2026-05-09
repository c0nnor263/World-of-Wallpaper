package com.doodle.core.navigation.args

import com.doodle.core.domain.enums.PaginatorKey
import com.doodle.core.navigation.Screens

data class PictureDetailsNavArgs(
    val selectedImageIndex: Int,
    val paginatorKey: PaginatorKey,
    val searchQuery: String = "",
    val isPremium: Boolean = false,
) {
    fun toScreen(): Screens.Details {
        return Screens.Details(
            selectedImageIndex = selectedImageIndex,
            pagingKey = paginatorKey.name,
            query = searchQuery,
            isPremium = isPremium
        )
    }
}
