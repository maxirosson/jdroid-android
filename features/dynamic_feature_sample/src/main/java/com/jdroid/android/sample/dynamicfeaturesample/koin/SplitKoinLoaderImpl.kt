package com.jdroid.android.sample.dynamicfeaturesample.koin

import com.jdroid.android.google.splitinstall.SplitKoinLoader
import com.jdroid.android.sample.dynamicfeaturesample.DynamicFeatureSampleSampleApiImpl
import com.jdroid.android.sample.google.dynamicfeature.DynamicFeatureSampleApi
import org.koin.core.module.Module
import org.koin.dsl.module

object SplitKoinLoaderImpl: SplitKoinLoader() {

	override fun createKoinModule(): Module {
		return module {
			single<DynamicFeatureSampleApi> { DynamicFeatureSampleSampleApiImpl() }
		}
	}
}