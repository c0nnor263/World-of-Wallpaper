package com.doodle.core.domain.source.local.repository

import com.doodle.core.domain.model.local.StorageImageInfo

abstract class StorageManager {
    // TODO: Use app name from resources
    protected val APP_NAME: String = "World Of Wallpapers"
    abstract suspend fun saveImage(info: StorageImageInfo): String?
    abstract suspend fun isFileExists(path: String): Boolean

    protected fun createFileName(info: StorageImageInfo): String {
        return "${info.id}_${info.userId}.${info.extension}"
    }

    protected fun createRelativePath(): String {
        return "Pictures/$APP_NAME/"
    }
}
