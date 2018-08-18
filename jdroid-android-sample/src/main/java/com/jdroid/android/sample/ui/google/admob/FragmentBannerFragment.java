package com.jdroid.android.sample.ui.google.admob;

import com.google.android.gms.ads.AdSize;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.firebase.admob.AdMobAppModule;
import com.jdroid.android.firebase.admob.AdMobFragmentDelegate;
import com.jdroid.android.firebase.admob.helpers.BaseAdViewHelper;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.application.AndroidAppContext;

public class FragmentBannerFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.empty_fragment;
	}

	@Override
	public FragmentDelegate createFragmentDelegate(AppModule appModule) {
		if (appModule instanceof AdMobAppModule) {
			return new AdMobFragmentDelegate(this) {
				@Override
				public void initBaseAdViewHelper(BaseAdViewHelper baseAdViewHelper) {
					baseAdViewHelper.setAdSize(AdSize.BANNER);
					baseAdViewHelper.setAdUnitId(AndroidAppContext.SAMPLE_BANNER_AD_UNIT_ID);
				}
			};
		} else {
			return super.createFragmentDelegate(appModule);
		}
	}
}
