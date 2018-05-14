package com.jdroid.android.firebase.admob.interstitial;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.firebase.admob.AdMobActivityDelegate;
import com.jdroid.android.firebase.admob.AdMobAppModule;
import com.jdroid.android.firebase.admob.helpers.InterstitialAdHelper;

public abstract class InterstitialAction {
	
	public void start(Context context) {
		start((ActivityIf)context);
	}
	
	public void start(ActivityIf activityIf) {
		InterstitialAdHelper interstitialAdHelper = ((AdMobActivityDelegate)(activityIf).getActivityDelegate(AdMobAppModule.get())).getInterstitialAdHelper();
		if (interstitialAdHelper != null) {
			Boolean displayed = interstitialAdHelper.displayInterstitial(false, new AdListener() {
				@Override
				public void onAdClosed() {
					onAction();
				}
			});
			if (!displayed) {
				onAction();
			}
		} else {
			onAction();
		}
	}
	
	protected abstract void onAction();
}
