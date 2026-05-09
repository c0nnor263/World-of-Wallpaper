package com.doodle.core.database


import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import androidx.core.net.toUri
import com.doodle.core.domain.di.IoDispatcher
import com.doodle.core.domain.model.local.StorageImageInfo
import com.doodle.core.domain.source.local.repository.StorageManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single(binds = [StorageManager::class])
class AndroidStorageManager(
    private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : StorageManager() {
    override suspend fun saveImage(info: StorageImageInfo): String? =
        withContext(ioDispatcher) {
            val contentResolver = context.contentResolver
            val values = createValues(
                appName = APP_NAME,
                info = info
            )

            val uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            ) ?: return@withContext null

            try {
                contentResolver.openOutputStream(uri)?.use { stream ->
                    stream.write(info.byteArray)
                }

                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                contentResolver.update(uri, values, null, null)
                uri.toString()
            } catch (_: Exception) {
                contentResolver.delete(uri, null, null)
                null
            }
        }

    override suspend fun isFileExists(path: String): Boolean =
        withContext(ioDispatcher) {
            runCatching {
                val contentResolver: ContentResolver = context.contentResolver
                contentResolver.openInputStream(path.toUri())?.use {
                    true
                } ?: false
            }.getOrDefault(false)
        }


    private fun createValues(
        appName: String,
        info: StorageImageInfo
    ): ContentValues {
        val timestamp = System.currentTimeMillis()

        return ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.DATE_ADDED, timestamp / 1000)
            put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
            put(MediaStore.Images.Media.DISPLAY_NAME, createFileName(info))
            put(MediaStore.Images.Media.RELATIVE_PATH, createRelativePath())
            put(MediaStore.Images.Media.IS_PENDING, true)
        }
    }
}
