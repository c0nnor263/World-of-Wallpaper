package com.doodle.turboracing3.presentation.composables

import coil3.PlatformContext
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun imageCacheDirectory(context: PlatformContext): Path {
    val cacheDir = NSFileManager.defaultManager
        .URLsForDirectory(
            directory = NSCachesDirectory,
            inDomains = NSUserDomainMask
        )
        .first()
        .toString()
        .removePrefix("file://")
    return "$cacheDir/image_cache".toPath()
}