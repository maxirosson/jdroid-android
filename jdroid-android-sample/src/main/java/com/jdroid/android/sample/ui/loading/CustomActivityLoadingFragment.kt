package com.jdroid.android.sample.ui.loading

import android.os.Bundle

import com.jdroid.android.activity.ActivityIf
import com.jdroid.android.loading.ActivityLoading
import com.jdroid.android.sample.R
import com.jdroid.android.utils.ToastUtils

class CustomActivityLoadingFragment : BlockingLoadingFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getActivityIf()!!.setLoading(object : ActivityLoading {

            override fun show(activityIf: ActivityIf) {
                ToastUtils.showToast(R.string.showLoading)
            }

            override fun dismiss(activityIf: ActivityIf) {
                ToastUtils.showToast(R.string.dismissLoading)
            }
        })
    }
}
