package com.doodle.core.ui.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doodle.core.ui.theme.WallpapersTheme

@Composable
fun CardButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.inversePrimary)
    ) {
        Button(onClick = onClick, shape = RectangleShape) {
            text()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardButtonPreview() {
    WallpapersTheme {
        CardButton(
            onClick = {},
            text = {
                Text(
                    "Go to home screen",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
            }
        )
    }
}
