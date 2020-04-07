package com.jdroid.android.loading

import android.app.Dialog
import android.content.Context

import com.jdroid.android.activity.ActivityIf

class DefaultBlockingLoading : BlockingLoading() {

    private var loadingDialog: Dialog? = null

    override fun show(activityIf: ActivityIf) {
        if (!activityIf.isActivityDestroyed() && (loadingDialog == null || !loadingDialog!!.isShowing)) {
            loadingDialog = LoadingDialog(activityIf as Context)
            loadingDialog?.setCancelable(isCancelable!!)
            loadingDialog?.show()
        }
    }

    override fun dismiss(activityIf: ActivityIf) {
        loadingDialog?.dismiss()
        loadingDialog = null
    }
}
