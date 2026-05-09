package com.doodle.core.domain.source

actual object PlatformFileExporter {
    actual fun exportFile(
        path: String,
        onExported: () -> Unit,
        onCancelled: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        onExported()
    }
}