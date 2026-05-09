package com.doodle.feature.splash.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.doodle.core.navigation.Screens
import com.doodle.core.ui.state.LocalRemoveAdsStatus
import com.doodle.core.ui.theme.WallpapersTheme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import worldofwallpapers.core.ui.generated.resources.app_name
import worldofwallpapers.core.ui.generated.resources.portrait_background
import worldofwallpapers.feature.splash.generated.resources.Res
import worldofwallpapers.feature.splash.generated.resources.your_world

const val SplashScreenTag = "SplashScreen"

fun NavGraphBuilder.splashScreen(
    onNavigateToHome: () -> Unit
) {
    composable<Screens.Splash>(
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        val viewModel: SplashScreenViewModel = koinViewModel()
        SplashScreen(viewModel, onNavigateToHome)
    }
}

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel,
    onNavigateToHome: () -> Unit
) {
    val removeAdsStatus = LocalRemoveAdsStatus.current
//    val adStatus = viewModel.appOpenAdStatus.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
//        when (adStatus.value) {
//            AdStatus.LOADING -> viewModel.incrementAppOpenTimes()
//            else -> {
//                if (removeAdsStatus.isNotPurchased()) {
//                    val activity = context as ComponentActivity
//                    viewModel.showAppOpenAd(activity)
//                }
        delay(2000L)
        onNavigateToHome()
//            }
//        }
    }
    SplashScreenContent()
}

@Composable
fun SplashScreenContent(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )
    val brush = remember(offset) {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val widthOffset = size.width * offset
                val heightOffset = size.height * offset
                return LinearGradientShader(
                    colors = listOf(
                        Color.White,
                        Color.Yellow.copy(0.5F),
                        Color.Red.copy(0.3F),
                        Color.Blue.copy(0.3F),
                        Color.White
                    ),
                    from = Offset(widthOffset, heightOffset),
                    to = Offset(widthOffset + size.width, heightOffset + size.height),
                    tileMode = TileMode.Mirror
                )
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                contentDescription = SplashScreenTag
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(worldofwallpapers.core.ui.generated.resources.Res.drawable.portrait_background),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            alpha = 0.9F
        )
        Text(
            text = stringResource(worldofwallpapers.core.ui.generated.resources.Res.string.app_name),
            style = MaterialTheme.typography.displayLarge.copy(
                shadow = Shadow(
                    Color.Black,
                    offset = Offset(
                        1f,
                        15.0F
                    ),
                    blurRadius = 24f
                ),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 128.dp)
        )
        Text(
            text = stringResource(Res.string.your_world),
            style = MaterialTheme.typography.headlineMedium.copy(
                shadow = Shadow(
                    Color.Black,
                    offset = Offset(
                        1f,
                        20.0F
                    ),
                    blurRadius = 24f
                ),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                brush = brush
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp)
        )
    }
}

@Preview
@Composable
fun SplashScreenContentPreview() {
    WallpapersTheme {
        SplashScreenContent()
    }
}
