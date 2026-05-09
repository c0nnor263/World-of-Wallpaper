package com.doodle.core.domain.source

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIViewController
import platform.darwin.NSObject

actual object PlatformFileExporter {

    private var activeDelegate: DocumentPickerDelegate? = null

    actual fun exportFile(
        path: String,
        onExported: () -> Unit,
        onCancelled: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        val url = NSURL.fileURLWithPath(path)

        val picker = UIDocumentPickerViewController(
            forExportingURLs = listOf(url),
            asCopy = true
        )

        val delegate = DocumentPickerDelegate(
            onExported = {
                activeDelegate = null
                onExported()
            },
            onCancelled = {
                activeDelegate = null
                onCancelled()
            }
        )

        activeDelegate = delegate
        picker.delegate = delegate

        val rootViewController = UIApplication.sharedApplication
            .keyWindow
            ?.rootViewController

        val topViewController = rootViewController?.topMostViewController()

        if (topViewController == null) {
            activeDelegate = null
            onFailed("Unable to open iOS file exporter")
            return
        }

        topViewController.presentViewController(
            picker,
            animated = true,
            completion = null
        )
    }

    private fun UIViewController.topMostViewController(): UIViewController {
        val presented = presentedViewController
        return presented?.topMostViewController() ?: this
    }
}

private class DocumentPickerDelegate(
    private val onExported: () -> Unit,
    private val onCancelled: () -> Unit
) : NSObject(), UIDocumentPickerDelegateProtocol {

    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>
    ) {
        onExported()
    }

    override fun documentPickerWasCancelled(
        controller: UIDocumentPickerViewController
    ) {
        onCancelled()
    }
}