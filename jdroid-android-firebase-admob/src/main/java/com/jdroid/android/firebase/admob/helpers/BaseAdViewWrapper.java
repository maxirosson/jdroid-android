package com.jdroid.android.firebase.admob.helpers;

import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class BaseAdViewWrapper {

	private View baseAdView;

	public BaseAdViewWrapper(View baseAdView) {
		this.baseAdView = baseAdView;
	}

	public void setAdUnitId(String adUnitId) {
		((AdView)baseAdView).setAdUnitId(adUnitId);
	}

	public void setAdSize(AdSize adSize) {
		((AdView)baseAdView).setAdSize(adSize);
	}

	public void setAdListener(AdListener adListener) {
		((AdView)baseAdView).setAdListener(adListener);
	}

	public void loadAd(AdRequest adRequest) {
		((AdView)baseAdView).loadAd(adRequest);
	}

	public void pause() {
		((AdView)baseAdView).pause();
	}

	public void resume() {
		((AdView)baseAdView).resume();
	}

	public void destroy() {
		((AdView)baseAdView).destroy();
	}

	public View getBaseAdView() {
		return baseAdView;
	}
}
