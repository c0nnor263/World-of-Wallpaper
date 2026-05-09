package com.doodle.core.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import kotlinx.serialization.Serializable


@Serializable
sealed class Screens() {
    @Serializable
    data object Splash : Screens()

    @Serializable
    data object Home : Screens()

    @Serializable
    data object Favorites : Screens()

    @Serializable
    data class Search(
        val query: String?
    ) : Screens()

    @Serializable
    data class Details(
        val selectedImageIndex: Int,
        val pagingKey: String,
        val query: String = "",
        val isPremium: Boolean = false,
    ) : Screens()

}

fun NavBackStackEntry?.isPermittedForAppOpenAd(): Boolean {
    return this?.run {
        destination.hasRoute<Screens.Search>() ||
                destination.hasRoute<Screens.Details>() ||
                destination.hasRoute<Screens.Home>()
    } ?: false
}
