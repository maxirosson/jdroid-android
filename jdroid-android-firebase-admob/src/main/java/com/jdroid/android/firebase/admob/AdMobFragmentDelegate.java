package com.jdroid.android.firebase.admob;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jdroid.android.firebase.admob.helpers.AdViewHelper;
import com.jdroid.android.firebase.admob.helpers.BaseAdViewHelper;
import com.jdroid.android.fragment.FragmentDelegate;

public class AdMobFragmentDelegate extends FragmentDelegate {

	private BaseAdViewHelper baseAdViewHelper;

	public AdMobFragmentDelegate(Fragment fragment) {
		super(fragment);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		if (AdMobAppModule.getAdMobAppContext().areAdsEnabled()) {
			baseAdViewHelper = createBaseAdViewHelper();
			if (baseAdViewHelper != null) {
				initBaseAdViewHelper(baseAdViewHelper);
				baseAdViewHelper.loadAd(getFragment().getActivity(), view.findViewById(getAdViewContainerId()));
			}
		}
	}

	protected int getAdViewContainerId() {
		return R.id.adViewContainer;
	}

	@Override
	public void onResume() {
		if (baseAdViewHelper != null) {
			baseAdViewHelper.onResume();
		}
	}

	@Override
	public void onBeforePause() {
		if (baseAdViewHelper != null) {
			baseAdViewHelper.onBeforePause();
		}
	}

	@Override
	public void onBeforeDestroy() {
		if (baseAdViewHelper != null) {
			baseAdViewHelper.onBeforeDestroy();
		}
	}

	@Nullable
	public BaseAdViewHelper createBaseAdViewHelper() {
		return new AdViewHelper();
	}

	public void initBaseAdViewHelper(BaseAdViewHelper adHelper) {
		// Do nothing
	}

	@Nullable
	public BaseAdViewHelper getBaseAdViewHelper() {
		return baseAdViewHelper;
	}
}
