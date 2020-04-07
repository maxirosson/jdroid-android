package com.jdroid.android.firebase.admob

import android.content.Context
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.debug.DebugSettingsHelper
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback
import com.jdroid.android.utils.SharedPreferencesHelper

class AdMobDebugAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onProviderInit(context: Context) {
        DebugSettingsHelper.addPreferencesAppender(AdsDebugPrefsAppender())
    }

    override fun onCreate(context: Context) {
        AppExecutors.diskIOExecutor.execute {
            // This is required to initialize the prefs to display on the debug settings
            if (!SharedPreferencesHelper.get().hasPreference(AdMobRemoteConfigParameter.ADS_ENABLED.getKey())) {
                SharedPreferencesHelper.get().savePreferenceAsync(
                    AdMobRemoteConfigParameter.ADS_ENABLED.getKey(),
                    AdMobAppModule.adMobAppContext.areAdsEnabledByDefault()
                )
            }
            if (!SharedPreferencesHelper.get().hasPreference(AdMobAppContext.TEST_AD_UNIT_ID_ENABLED)) {
                SharedPreferencesHelper.get().savePreferenceAsync(AdMobAppContext.TEST_AD_UNIT_ID_ENABLED, true)
            }
        }
    }
}
