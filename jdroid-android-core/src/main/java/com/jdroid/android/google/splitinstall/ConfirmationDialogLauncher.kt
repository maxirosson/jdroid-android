package com.jdroid.android.google.splitinstall

import android.app.Activity
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallSessionState

class ConfirmationDialogLauncher(
    private val splitInstallManager: SplitInstallManager,
    private val splitInstallSessionState: SplitInstallSessionState
) {

    fun startConfirmationDialogForResult(activity: Activity, requestCode: Int) {
        splitInstallManager.startConfirmationDialogForResult(splitInstallSessionState, activity, requestCode)
    }
}