package com.jdroid.android.sample.ui.google.admob;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.firebase.admob.interstitial.InterstitialAction;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.firebase.remoteconfig.AndroidRemoteConfigParameter;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.remoteconfig.RemoteConfigParameter;

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
				ActivityLauncher.startActivity(getActivity(), FragmentBannerActivity.class);
			}
		});

		findView(R.id.adReycler).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.startActivity(getActivity(), AdRecyclerActivity.class);
			}
		});

		findView(R.id.activityBanner).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.startActivity(getActivity(), ActivityBannerActivity.class);
			}
		});

		findView(R.id.displayInterstitial).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new InterstitialAction() {
					@Override
					protected void onAction() {
						ToastUtils.showToast(R.string.interstitialAction);
					}

					@NonNull
					@Override
					protected RemoteConfigParameter getEnabledRemoteConfigParameter() {
						return AndroidRemoteConfigParameter.INTERSTITIAL_ENABLED;
					}
				}.start(getActivityIf());
			}
		});

		findView(R.id.houseAds).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.startActivity(getActivity(), HouseAdsActivity.class);
			}
		});
	}
}
