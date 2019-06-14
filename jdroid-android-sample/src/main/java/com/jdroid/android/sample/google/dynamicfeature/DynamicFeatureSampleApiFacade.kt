package com.jdroid.android.sample.google.dynamicfeature

import com.google.android.play.core.tasks.OnFailureListener
import com.google.android.play.core.tasks.OnSuccessListener
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.google.splitinstall.SplitInstallHelper
import com.jdroid.android.utils.ToastUtils

interface DynamicFeatureSampleApi {

    fun sampleCall()
}

object DynamicFeatureSampleApiFacade : DynamicFeatureSampleApi {

    const val MODULE_NAME = "dynamic_feature_sample"

    override fun sampleCall() {

        val onSuccessListener = OnSuccessListener<Any> {
            SplitInstallHelper.getFeatureApi<DynamicFeatureSampleApi>(MODULE_NAME).sampleCall()
        }
        val onFailureListener = OnFailureListener {
            ToastUtils.showToastOnUIThread("FakeFeatureApiFacade onFailureListener called")
        }

        AppExecutors.getNetworkIOExecutor().execute(object : Runnable {
            override fun run() {
                SplitInstallHelper.installModuleSilently(MODULE_NAME, onSuccessListener, onFailureListener)
            }
        })
    }
}