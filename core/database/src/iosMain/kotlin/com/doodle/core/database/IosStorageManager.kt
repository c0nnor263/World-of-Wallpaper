package com.doodle.core.database

import com.doodle.core.domain.AppLogger
import com.doodle.core.domain.model.local.StorageImageInfo
import com.doodle.core.domain.source.local.repository.StorageManager
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.koin.core.annotation.Single
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dataWithBytes
import platform.Foundation.writeToFile
import kotlin.time.Clock

@Single(binds = [StorageManager::class])
class IosStorageManager : StorageManager() {
    override suspend fun saveImage(info: StorageImageInfo): String? {
        val bytes = info.byteArray

        if (bytes == null || bytes.isEmpty()) {
            AppLogger.e("TAG", "Image bytes are null or empty")
            return null
        }

        val imageId = info.id ?: Clock.System.now().toEpochMilliseconds()
        val userId = info.userId ?: 0
        val fileName = "${imageId}_${userId}.${info.extension}"
        val path = "${documentsDirectoryPath()}/$fileName"

        val data = bytes.toNSData()
        val success = data.writeToFile(path, atomically = true)

        AppLogger.i("TAG", "Saving image to path: $path")
        AppLogger.i("TAG", "Save success: $success")
        AppLogger.i(
            "TAG",
            "File exists after save: ${NSFileManager.defaultManager.fileExistsAtPath(path)}"
        )

        return if (success) path else null
    }

    override suspend fun isFileExists(path: String): Boolean {
        return NSFileManager.defaultManager.fileExistsAtPath(path)
    }

    private fun documentsDirectoryPath(): String {
        val paths = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory,
            NSUserDomainMask,
            true
        )

        return paths.first() as String
    }
}

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData =
    usePinned {
        NSData.dataWithBytes(
            bytes = it.addressOf(0),
            length = size.toULong()
        )
    }