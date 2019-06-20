package com.jdroid.android.sample.google.dynamicfeature

import com.jdroid.android.google.splitinstall.SplitInstallHelper
import com.jdroid.android.utils.ToastUtils

interface DynamicFeatureSampleApi {

    fun sampleCall()
}

object DynamicFeatureSampleApiFacade : AbstractFeatureApi(), DynamicFeatureSampleApi {

    const val MODULE_NAME = "dynamic_feature_sample"

    override fun sampleCall() {
        invokeFeatureAsync({
            SplitInstallHelper.getFeatureApi<DynamicFeatureSampleApi>(getModuleName()).sampleCall()
        }, {
            ToastUtils.showToastOnUIThread("FakeFeatureApiFacade onFailureListener called")
        })
    }

    override fun getModuleName(): String {
        return MODULE_NAME
    }
}