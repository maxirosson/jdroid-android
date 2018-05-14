package com.jdroid.android.sample.ui.google.admob;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.ads.AdListener;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.firebase.admob.AdMobActivityDelegate;
import com.jdroid.android.firebase.admob.AdMobAppModule;
import com.jdroid.android.firebase.admob.helpers.InterstitialAdHelper;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.ToastUtils;

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
				InterstitialAdHelper interstitialAdHelper = ((AdMobActivityDelegate)getActivityIf().getActivityDelegate(AdMobAppModule.get())).getInterstitialAdHelper();
				if (interstitialAdHelper != null) {
					Boolean adDisplayed = interstitialAdHelper.displayInterstitial(false, new AdListener() {
						@Override
						public void onAdClosed() {
							ToastUtils.showToast(R.string.interstitialClosed);
						}
					});
					if (!adDisplayed) {
						ToastUtils.showToast(R.string.interstitialNotLoaded);
					}
				} else {
					ToastUtils.showToast(R.string.adsNotEnabled);
				}
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
