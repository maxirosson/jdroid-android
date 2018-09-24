package com.jdroid.android.sample.ui.google.admob;

import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdSize;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.firebase.admob.AdMobActivityDelegate;
import com.jdroid.android.firebase.admob.AdMobAppModule;
import com.jdroid.android.firebase.admob.helpers.BaseAdViewHelper;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.application.AndroidAppContext;

public class ActivityBannerActivity extends FragmentContainerActivity {

	@Override
	public int getContentView() {
		return R.layout.activity_banner_activity;
	}

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return ActivityBannerFragment.class;
	}

	@Override
	public ActivityDelegate createActivityDelegate(AppModule appModule) {
		if (appModule instanceof AdMobAppModule) {
			return new AdMobActivityDelegate(this) {

				@Override
				public void initBaseAdViewHelper(BaseAdViewHelper baseAdViewHelper) {
					baseAdViewHelper.setAdSize(AdSize.BANNER);
					baseAdViewHelper.setAdUnitId(AndroidAppContext.SAMPLE_BANNER_AD_UNIT_ID);
				}
			};
		} else {
			return super.createActivityDelegate(appModule);
		}
	}
}
