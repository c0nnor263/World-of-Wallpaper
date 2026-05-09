package com.doodle.core.domain.source

expect object PlatformFileExporter {
    fun exportFile(
        path: String,
        onExported: () -> Unit,
        onCancelled: () -> Unit,
        onFailed: (String) -> Unit
    )
}