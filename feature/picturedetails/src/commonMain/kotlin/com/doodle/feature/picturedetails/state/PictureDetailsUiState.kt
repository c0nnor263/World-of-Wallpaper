package com.doodle.feature.picturedetails.state

import androidx.compose.runtime.compositionLocalOf
import com.doodle.feature.picturedetails.presentation.PictureDetailsViewModel

val LocalPictureDetailsUiState = compositionLocalOf<PictureDetailsViewModel.UiState?> {
    null
}
