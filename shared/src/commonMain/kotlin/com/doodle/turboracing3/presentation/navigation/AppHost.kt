package com.doodle.turboracing3.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.doodle.core.navigation.Screens
import com.doodle.feature.favorites.presentation.favoritesScreen
import com.doodle.feature.home.presentation.homeScreen
import com.doodle.feature.picturedetails.presentation.detailsScreen
import com.doodle.feature.search.presentation.searchScreen
import com.doodle.feature.splash.presentation.splashScreen

@Composable
fun AppHost(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screens.Splash
    ) {

        splashScreen(
            onNavigateToHome = {
                navController.navigate(Screens.Home) {
                    popUpTo(Screens.Home) {
                        inclusive = true
                    }
                }
            }
        )

        homeScreen(
            onNavigateToFavorites = {
                navController.navigate(Screens.Favorites)
            },
            onNavigateToSearch = { searchNavArgs ->
                val screen = Screens.Search(
                    query = searchNavArgs?.query
                )
                navController.navigate(screen)
            },
            onNavigateToDetails = { pictureDetailsNavArgs ->
                val screen = pictureDetailsNavArgs.toScreen()
                navController.navigate(screen)
            }
        )

        detailsScreen(
            onNavigateToSearch = { searchNavArgs ->
                val screen = searchNavArgs?.toScreen() ?: Screens.Search
                navController.navigate(screen)
            },
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        searchScreen(
            onNavigateToDetails = { pictureDetailsNavArgs ->
                val screen = pictureDetailsNavArgs.toScreen()
                navController.navigate(screen)
            },
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        favoritesScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToHome = {
                navController.navigate(Screens.Home) {
                    popUpTo(Screens.Home) {
                        inclusive = true
                    }
                }
            },
            onNavigateToDetails = { pictureDetailsNavArgs ->
                val screen = pictureDetailsNavArgs.toScreen()
                navController.navigate(screen)
            }
        )
    }
}
