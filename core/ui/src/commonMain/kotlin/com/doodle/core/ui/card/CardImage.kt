package com.doodle.core.ui.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doodle.core.ui.theme.WallpapersTheme
import org.jetbrains.compose.resources.painterResource
import worldofwallpapers.core.ui.generated.resources.Res
import worldofwallpapers.core.ui.generated.resources.yes_icon

@Composable
fun CardImage(
    modifier: Modifier = Modifier,
    image: @Composable () -> Unit
) {
    val primary = MaterialTheme.colorScheme.secondary
    val listOfColors = remember {
        listOf(primary.copy(0.3F), primary, primary.copy(0.3F))
    }

    Card(
        modifier = modifier
            .drawWithContent {
                drawContent()
                val gradient = Brush.radialGradient(
                    listOfColors,
                    center = Offset(size.width / 2, size.height / 2),
                    radius = size.width
                )
                drawRoundRect(
                    gradient,
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                    style = Stroke(width = 5.dp.toPx())
                )
                drawRoundRect(
                    Color.White,
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                    style = Stroke(width = 1.dp.toPx())
                )

            },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        image()
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun CardImagePreview() {
    WallpapersTheme {
        CardImage(modifier = Modifier.wrapContentSize()) {
            Image(
                modifier = Modifier
                    .size(300.dp)
                    .padding(16.dp),
                painter = painterResource(Res.drawable.yes_icon),
                contentDescription = null
            )
        }
    }
}
