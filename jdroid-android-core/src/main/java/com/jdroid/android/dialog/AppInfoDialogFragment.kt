package com.jdroid.android.dialog

import androidx.fragment.app.FragmentActivity

import com.jdroid.android.R
import com.jdroid.android.utils.ExternalAppsUtils

class AppInfoDialogFragment : AlertDialogFragment() {

    override fun onPositiveClick() {
        ExternalAppsUtils.openAppInfo()
    }

    companion object {

        fun show(fragmentActivity: FragmentActivity, titleResId: Int, messageResId: Int, permission: String) {
            val screenViewName = AppInfoDialogFragment::class.java.simpleName + "-" + permission
            show(
                fragmentActivity,
                AppInfoDialogFragment(),
                null,
                fragmentActivity.getString(titleResId),
                fragmentActivity.getString(messageResId),
                fragmentActivity.getString(R.string.jdroid_cancel),
                null,
                fragmentActivity.getString(R.string.jdroid_deviceSettings),
                true,
                screenViewName,
                null
            )
        }
    }
}
