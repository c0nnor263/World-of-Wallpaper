package com.doodle.core.navigation.args

import com.doodle.core.navigation.Screens

data class SearchNavArgs(
    val query: String
) {
    fun toScreen(): Screens.Search {
        return Screens.Search(
            query = query
        )
    }
}
