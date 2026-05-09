package com.doodle.feature.home.presentation.common

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.doodle.core.ui.ErrorMessage
import com.doodle.core.ui.NoRippleInteractionSource
import com.doodle.core.ui.scaleWithPressAnimation
import com.doodle.core.ui.theme.WallpapersTheme
import com.doodle.core.ui.tweenEasy
import com.doodle.core.ui.tweenMedium
import com.doodle.feature.home.domain.enums.TabPage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeTabLayout(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    isLoading: Boolean,
    errorMsg: String?,
    onErrorClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val tabRowEdgePadding = maxWidth - minWidth * 0.8F
        Column(modifier = Modifier.animateContentSize(tweenMedium())) {
            ScrollableTabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = pagerState.currentPage,
                edgePadding = tabRowEdgePadding,
                indicator = {
                    TabIndicator(
                        tabPositions = it,
                        tabPage = TabPage.entries[pagerState.currentPage],
                        isLoading = isLoading
                    )
                },
                divider = {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isLoading,
                        enter = slideInVertically(tweenEasy(easing = FastOutLinearInEasing)) { it },
                        exit = slideOutVertically(tweenEasy(easing = FastOutLinearInEasing)) { it }
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            ) {
                TabPage.entries.forEachIndexed { index, page ->
                    val selected = pagerState.currentPage == index
                    HomeTab(
                        tabPage = page,
                        visible = !isLoading || !selected,
                        selected = selected,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }
            ErrorMessage(errorMsg, onErrorClick)
        }
    }
}

@Composable
fun HomeTab(tabPage: TabPage, visible: Boolean, selected: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { NoRippleInteractionSource() }
    Tab(
        text = {
            androidx.compose.animation.AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tweenMedium()),
                exit = fadeOut(tweenMedium())
            ) {
                Text(
                    text = stringResource(tabPage.labelResource),
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        },
        selected = selected,
        onClick = onClick,
        interactionSource = interactionSource
    )
}


@Composable
fun TabIndicator(
    tabPositions: List<TabPosition>,
    tabPage: TabPage,
    isLoading: Boolean
) {
    val transition = updateTransition(
        tabPage,
        label = "Tab indicator"
    )
    val isTransiting = transition.isRunning || isLoading

    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            tweenEasy()
        },
        label = "Indicator left"
    ) { page ->
        tabPositions[page.ordinal].left
    }

    val indicatorRight by transition.animateDp(
        transitionSpec = {
            tweenEasy()
        },
        label = "Indicator right"
    ) { page ->
        tabPositions[page.ordinal].right
    }

    val borderAnimation by animateDpAsState(
        targetValue = if (isTransiting) 0.dp else 2.dp,
        animationSpec = tweenEasy(),
        label = "Border animation"
    )

    Box(
        modifier = Modifier
            .zIndex(-1F)
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .scaleWithPressAnimation(transition.isRunning),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(borderAnimation, MaterialTheme.colorScheme.primary)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isLoading,
                    enter = fadeIn(tweenMedium()),
                    exit = fadeOut(tweenMedium())
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun HomeTabPreview() {
    WallpapersTheme {
        val pagerState = rememberPagerState(initialPage = TabPage.LATEST.ordinal) {
            TabPage.entries.size
        }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(key1 = Unit) {
            delay(5000)
            isLoading = false
        }
        HomeTabLayout(
            pagerState = pagerState,
            isLoading = isLoading,
            errorMsg = "Error fetching images "
        ) {
        }
    }
}
