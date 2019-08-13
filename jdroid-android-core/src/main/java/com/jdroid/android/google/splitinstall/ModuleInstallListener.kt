package com.jdroid.android.google.splitinstall

import androidx.annotation.UiThread

@UiThread
interface ModuleInstallListener {

    fun onSuccess()

    fun onFailure(exception: Exception? = null)

    fun onRequiresUserConfirmation(confirmationDialogLauncher: ConfirmationDialogLauncher) {
        // Optional implementation
    }

    fun onDownloadInProgress(progress: Long, totalBytes: Long) {
        // Optional implementation
    }
}