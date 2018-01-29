package com.jdroid.android.sample.ui.google.admob;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.firebase.admob.AdMobActivityDelegate;
import com.jdroid.android.firebase.admob.AdMobAppModule;
import com.jdroid.android.sample.R;

public class AdsFragment extends AbstractFragment {
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.ads_fragment;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.fragmentBanner).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.launchActivity(FragmentBannerActivity.class);
			}
		});

		findView(R.id.adReycler).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.launchActivity(AdRecyclerActivity.class);
			}
		});

		findView(R.id.activityBanner).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.launchActivity(ActivityBannerActivity.class);
			}
		});

		findView(R.id.displayInterstitial).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((AdMobActivityDelegate)getActivityIf().getActivityDelegate(AdMobAppModule.get())).getInterstitialAdHelper().displayInterstitial(false);
			}
		});

		findView(R.id.houseAds).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.launchActivity(HouseAdsActivity.class);
			}
		});
	}
}
