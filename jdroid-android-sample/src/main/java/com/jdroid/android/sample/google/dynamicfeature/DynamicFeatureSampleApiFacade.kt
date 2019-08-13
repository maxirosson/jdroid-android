package com.jdroid.android.sample.google.dynamicfeature

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.google.splitinstall.SplitInstallHelper

interface DynamicFeatureSampleApi {

    fun sampleCall()
}

object DynamicFeatureSampleApiFacade : AbstractFeatureApi(), DynamicFeatureSampleApi {

    override val moduleName = "dynamic_feature_sample"

    override fun sampleCall() {
        try {
            SplitInstallHelper.getFeatureApi<DynamicFeatureSampleApi>(moduleName).sampleCall()
        } catch (e: Exception) {
            AbstractApplication.get().exceptionHandler.logHandledException(e)
        }
    }
}