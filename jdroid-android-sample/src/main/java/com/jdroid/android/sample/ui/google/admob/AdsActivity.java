package com.jdroid.android.sample.ui.google.admob;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.firebase.admob.AdMobActivityDelegate;
import com.jdroid.android.firebase.admob.AdMobAppModule;
import com.jdroid.android.firebase.admob.helpers.InterstitialAdHelper;
import com.jdroid.android.sample.application.AndroidAppContext;

public class AdsActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return AdsFragment.class;
	}

	@Override
	public ActivityDelegate createActivityDelegate(AppModule appModule) {
		if (appModule instanceof AdMobAppModule) {
			return new AdMobActivityDelegate(this) {

				@Nullable
				@Override
				public InterstitialAdHelper createInterstitialAdHelper() {
					return new InterstitialAdHelper();
				}

				@Override
				public void initInterstitialAdHelper(InterstitialAdHelper adHelper) {
					adHelper.setAdUnitId(AndroidAppContext.SAMPLE_INTERSTITIAL_AD_UNIT_ID);
				}
			};
		} else {
			return super.createActivityDelegate(appModule);
		}
	}
}
