package com.doodle.feature.home.domain.enums

import com.doodle.core.domain.enums.PaginatorKey
import org.jetbrains.compose.resources.StringResource
import worldofwallpapers.feature.home.generated.resources.Res
import worldofwallpapers.feature.home.generated.resources.tab_latest
import worldofwallpapers.feature.home.generated.resources.tab_popular
import worldofwallpapers.feature.home.generated.resources.tab_premium_editor_choice
import worldofwallpapers.feature.home.generated.resources.tab_tags

enum class TabPage(
    val labelResource: StringResource,
    val paginatorKey: PaginatorKey
) {
    TAGS(labelResource = Res.string.tab_tags, paginatorKey = PaginatorKey.TAGS),
    LATEST(labelResource = Res.string.tab_latest, paginatorKey = PaginatorKey.LATEST),
    POPULAR(labelResource = Res.string.tab_popular, paginatorKey = PaginatorKey.POPULAR),
    EDITORS_CHOICE(
        labelResource = Res.string.tab_premium_editor_choice,
        paginatorKey = PaginatorKey.EDITORS_CHOICE
    )
}
