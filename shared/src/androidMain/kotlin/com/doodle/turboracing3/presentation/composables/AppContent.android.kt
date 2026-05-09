package com.doodle.turboracing3.presentation.composables

import coil3.PlatformContext
import okio.Path
import okio.Path.Companion.toOkioPath

actual fun imageCacheDirectory(context: PlatformContext): Path {
    return context.cacheDir
        .resolve("image_cache")
        .toOkioPath()
}