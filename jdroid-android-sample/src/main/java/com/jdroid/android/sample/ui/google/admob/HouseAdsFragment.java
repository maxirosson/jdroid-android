package com.jdroid.android.sample.ui.google.admob;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.firebase.admob.AdMobAppModule;
import com.jdroid.android.firebase.admob.AdMobFragmentDelegate;
import com.jdroid.android.firebase.admob.HouseAdBuilder;
import com.jdroid.android.firebase.admob.helpers.BaseAdViewHelper;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.android.sample.R;

public class HouseAdsFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.house_ads_fragment;
	}

	@Override
	public FragmentDelegate createFragmentDelegate(AppModule appModule) {
		if (appModule instanceof AdMobAppModule) {
			return new AdMobFragmentDelegate(this) {
				@Override
				public void initBaseAdViewHelper(BaseAdViewHelper baseAdViewHelper) {
					baseAdViewHelper.setAdSize(AdSize.BANNER);
					baseAdViewHelper.setHouseAdBuilder(new HouseAdBuilder() {
						@Override
						public View build(Activity activity) {
							View view = super.build(activity);
							if (view == null) {
								view = new TextView(getActivity());
								((TextView)view).setText(R.string.houseAd);
							}
							return view;
						}
					});
				}
			};
		} else {
			return super.createFragmentDelegate(appModule);
		}
	}
}
