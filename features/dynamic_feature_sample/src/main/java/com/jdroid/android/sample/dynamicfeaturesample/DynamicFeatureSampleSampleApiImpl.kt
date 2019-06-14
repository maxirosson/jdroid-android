package com.jdroid.android.sample.dynamicfeaturesample

import com.jdroid.android.sample.google.dynamicfeature.DynamicFeatureSampleApi
import com.jdroid.android.utils.ToastUtils

class DynamicFeatureSampleSampleApiImpl: DynamicFeatureSampleApi {
	override fun sampleCall() {
		ToastUtils.showToastOnUIThread("Sample call !!!")
	}
}