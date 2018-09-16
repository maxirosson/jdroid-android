package com.jdroid.android.firebase.admob.interstitial;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.admob.AdMobActivityDelegate;
import com.jdroid.android.firebase.admob.AdMobAppModule;
import com.jdroid.android.firebase.admob.helpers.InterstitialAdHelper;
import com.jdroid.java.remoteconfig.RemoteConfigParameter;

public abstract class InterstitialAction {

	public void start(Context context) {
		start((ActivityIf)context);
	}

	public void start(ActivityIf activityIf) {
		Boolean enabled = AbstractApplication.get().getRemoteConfigLoader().getBoolean(getEnabledRemoteConfigParameter());
		if (enabled) {
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
		} else {
			onAction();
		}
	}

	protected abstract void onAction();

	@NonNull
	protected abstract RemoteConfigParameter getEnabledRemoteConfigParameter();
}
