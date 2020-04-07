package com.jdroid.android.exception

import android.app.Activity

class DefaultErrorDialogStrategy : ErrorDialogStrategy {

    private var goBackOnError: Boolean = false

    override fun onPositiveClick(activity: Activity?) {
        if (goBackOnError && activity != null) {
            activity.finish()
        }
    }

    fun setGoBackOnError(goBackOnError: Boolean) {
        this.goBackOnError = goBackOnError
    }
}
