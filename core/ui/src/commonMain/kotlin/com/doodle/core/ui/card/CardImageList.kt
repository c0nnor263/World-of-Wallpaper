package com.doodle.core.ui.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doodle.core.ui.ActionButton
import com.doodle.core.ui.FetchedImageItem
import com.doodle.core.ui.theme.WallpapersTheme
import com.doodle.core.ui.tweenMedium
import kotlinx.coroutines.launch
import worldofwallpapers.core.ui.generated.resources.Res
import worldofwallpapers.core.ui.generated.resources.arrow_upward_24px

@Composable
fun CardImageList(
    modifier: Modifier = Modifier,
    columns: StaggeredGridCells,
    isItemsEmpty: Boolean,
    onEmptyContent: @Composable () -> Unit = {},
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    content: LazyStaggeredGridScope.() -> Unit
) {
    if (isItemsEmpty) {
        onEmptyContent()
    } else {
        val scope = rememberCoroutineScope()
        val showUpButton by remember {
            derivedStateOf {
                state.firstVisibleItemIndex > 0
            }
        }

        Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {
            LazyVerticalStaggeredGrid(
                state = state,
                columns = columns,
                contentPadding = PaddingValues(8.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                content()
            }

            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                visible = showUpButton,
                enter = fadeIn(tweenMedium()) + scaleIn(tweenMedium()),
                exit = scaleOut(tweenMedium()) + fadeOut(tweenMedium())
            ) {
                ActionButton(
                    iconDrawableResource = Res.drawable.arrow_upward_24px,
                    isActive = showUpButton,
                    onActionClick = { _ ->
                        scope.launch {
                            state.animateScrollToItem(0)
                        }
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun CardImageListPreview() {
    WallpapersTheme {
        CardImageList(
            columns = StaggeredGridCells.Fixed(3),
            isItemsEmpty = false,
            onEmptyContent = {},
            content = {
                items(10) {
                    FetchedImageItem(
                        isPremium = false,
                        previewURL = "https://images.pexels.com/photos/2486168/pexels-photo-2486168.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                        modifier = Modifier.padding(4.dp)
                    ) {

                    }
                }
            }
        )
    }
}