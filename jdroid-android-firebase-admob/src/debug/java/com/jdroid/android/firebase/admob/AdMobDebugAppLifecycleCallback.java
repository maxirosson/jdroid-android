package com.jdroid.android.firebase.admob;

import android.content.Context;

import com.jdroid.android.concurrent.AppExecutors;
import com.jdroid.android.debug.DebugSettingsHelper;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.utils.SharedPreferencesHelper;

import static com.jdroid.android.firebase.admob.AdMobAppContext.TEST_AD_UNIT_ID_ENABLED;

public class AdMobDebugAppLifecycleCallback extends ApplicationLifecycleCallback {

	@Override
	public void onProviderInit(Context context) {
		DebugSettingsHelper.INSTANCE.addPreferencesAppender(new AdsDebugPrefsAppender());
	}

	@Override
	public void onCreate(Context context) {
		AppExecutors.INSTANCE.getDiskIOExecutor().execute(new Runnable() {
			@Override
			public void run() {
				// This is required to initialize the prefs to display on the debug settings
				if (!SharedPreferencesHelper.get().hasPreference(AdMobRemoteConfigParameter.ADS_ENABLED.getKey())) {
					SharedPreferencesHelper.get().savePreferenceAsync(AdMobRemoteConfigParameter.ADS_ENABLED.getKey(), AdMobAppModule.getAdMobAppContext().areAdsEnabledByDefault());
				}
				if (!SharedPreferencesHelper.get().hasPreference(TEST_AD_UNIT_ID_ENABLED)) {
					SharedPreferencesHelper.get().savePreferenceAsync(TEST_AD_UNIT_ID_ENABLED, true);
				}
			}
		});
	}
}
