package com.doodle.core.domain.enums

enum class PaginatorKey(val order: String = "") {
    FAVORITES("favorites"),
    TAGS("tags"),
    LATEST("latest"),
    POPULAR("popular"),
    SEARCH("search"),
    EDITORS_CHOICE("editors_choice"),
}
