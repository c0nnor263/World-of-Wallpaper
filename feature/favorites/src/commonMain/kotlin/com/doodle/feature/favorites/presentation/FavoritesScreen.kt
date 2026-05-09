package com.doodle.feature.favorites.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil3.compose.AsyncImage
import com.doodle.core.domain.enums.PaginatorKey
import com.doodle.core.domain.paginator.PaginatorWrapper
import com.doodle.core.domain.paginator.PaginatorWrapperUiState
import com.doodle.core.navigation.Screens
import com.doodle.core.navigation.args.PictureDetailsNavArgs
import com.doodle.core.ui.ApplicationScaffold
import com.doodle.core.ui.NavigationIcon
import com.doodle.core.ui.card.CardButton
import com.doodle.core.ui.card.CardImage
import com.doodle.core.ui.card.CardImageList
import com.doodle.core.ui.platformDisplayCutoutPadding
import com.doodle.core.ui.tweenMedium
import com.jamal_aliev.paginator.compose.paginated
import com.jamal_aliev.paginator.compose.rememberPaginated
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import worldofwallpapers.core.ui.generated.resources.arrow_left_alt_24px
import worldofwallpapers.feature.favorites.generated.resources.Res
import worldofwallpapers.feature.favorites.generated.resources.check_more_wallpapers
import worldofwallpapers.feature.favorites.generated.resources.favorites
import worldofwallpapers.feature.favorites.generated.resources.go_to_home_screen
import worldofwallpapers.feature.favorites.generated.resources.your_favorites_list_is_empty

fun NavGraphBuilder.favoritesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToDetails: (PictureDetailsNavArgs) -> Unit
) {
    composable<Screens.Favorites>(
        enterTransition = {
            fadeIn(animationSpec = tweenMedium()) +
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tweenMedium()
                    )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tweenMedium()
            ) +
                    fadeOut(animationSpec = tweenMedium())
        }
    ) {
        val viewModel: FavoritesScreenViewModel = koinViewModel()
        FavoritesScreen(
            viewModel = viewModel,
            onNavigateBack = onNavigateBack,
            onNavigateToDetails = onNavigateToDetails,
            onNavigateToHome = onNavigateToHome
        )
    }
}


@Composable
fun FavoritesScreen(
    viewModel: FavoritesScreenViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToDetails: (PictureDetailsNavArgs) -> Unit
) {
    val paginator = viewModel.paginator

    ApplicationScaffold(
        title = stringResource(Res.string.favorites),
        navigationIcon = {
            NavigationIcon(
                painter = painterResource(worldofwallpapers.core.ui.generated.resources.Res.drawable.arrow_left_alt_24px),
                onClick = onNavigateBack
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .platformDisplayCutoutPadding()
    ) { innerPadding ->
        FavoritesScreenContent(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding()),
            paginator = paginator,
            onCheckImageExisting = viewModel::onCheckImageExisting,
            onNavigateToDetails = onNavigateToDetails,
            onNavigateToHome = onNavigateToHome
        )
    }
}

@Composable
fun FavoritesScreenContent(
    modifier: Modifier = Modifier,
    paginator: PaginatorWrapper<*>,
    onCheckImageExisting: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToDetails: (PictureDetailsNavArgs) -> Unit
) {
    val staggeredGridListState = rememberLazyStaggeredGridState()
    val pagingUiState = paginator.uiState.collectAsStateWithLifecycle(null).value
    val paged = paginator.raw.rememberPaginated(state = staggeredGridListState)
    val contentItems = (pagingUiState as? PaginatorWrapperUiState.Content)?.items

    LaunchedEffect(contentItems?.count()) {
        if (!contentItems.isNullOrEmpty()) {
            onCheckImageExisting()
        }
    }

    CardImageList(
        modifier = modifier,
        state = staggeredGridListState,
        columns = StaggeredGridCells.Fixed(3),
        isItemsEmpty = (contentItems?.count() ?: 0) == 0 ||
                pagingUiState is PaginatorWrapperUiState.Empty,
        onEmptyContent = {
            FavoritesEmptyListContent(onNavigateToHome = onNavigateToHome)
        },
        content = {
            val items = contentItems ?: emptyList()
            paginated(paged) {
                items(items.count()) { index ->
                    val image = items[index]
                    val uri: String = image.previewURL ?: ""
                    FavoriteImageItem(
                        fileURI = uri,
                        onNavigateToDetails = {
                            onNavigateToDetails(
                                PictureDetailsNavArgs(
                                    selectedImageIndex = index,
                                    paginatorKey = PaginatorKey.FAVORITES
                                )
                            )
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun FavoriteImageItem(
    modifier: Modifier = Modifier,
    fileURI: String,
    onNavigateToDetails: () -> Unit
) {
    CardImage(modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .defaultMinSize(minHeight = 200.dp)
                .clickable(onClick = onNavigateToDetails),
            model = fileURI,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun FavoritesEmptyListContent(modifier: Modifier = Modifier, onNavigateToHome: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(Res.string.your_favorites_list_is_empty),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(Res.string.check_more_wallpapers),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        CardButton(onClick = { onNavigateToHome() }) {
            Text(
                stringResource(Res.string.go_to_home_screen),
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )
        }
    }
}
