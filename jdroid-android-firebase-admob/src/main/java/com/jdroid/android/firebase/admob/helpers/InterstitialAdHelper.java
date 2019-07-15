package com.jdroid.android.firebase.admob.helpers;

import android.app.Activity;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.jdroid.android.firebase.admob.AdMobAppModule;
import com.jdroid.android.firebase.admob.AdsStats;
import com.jdroid.android.location.LocationHelper;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.java.exception.UnexpectedException;

import java.util.List;

public class InterstitialAdHelper implements AdHelper {

	private static final String TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";

	private InterstitialAd interstitial;
	private Boolean displayInterstitial = false;
	private String interstitialAdUnitId;
	private List<AdListener> adListeners;

	public InterstitialAdHelper() {
		interstitialAdUnitId = AdMobAppModule.getAdMobAppContext().getDefaultAdUnitId();
	}

	private AdRequest.Builder createBuilder() {
		final AdRequest.Builder builder = new AdRequest.Builder();
		if (!AppUtils.INSTANCE.isReleaseBuildType()) {
			builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
			for (String deviceId : AdMobAppModule.getAdMobAppContext().getTestDevicesIds()) {
				builder.addTestDevice(deviceId);
			}
		}
		builder.setLocation(LocationHelper.get().getLocation());
		return builder;
	}

	@Override
	public void loadAd(Activity activity, ViewGroup adViewContainer) {
		if (AdMobAppModule.getAdMobAppContext().isInterstitialEnabled()) {
			interstitial = new InterstitialAd(activity);

			if (interstitialAdUnitId == null) {
				throw new UnexpectedException("Missing interstitial ad unit ID");
			}

			if (!AppUtils.INSTANCE.isReleaseBuildType() && AdMobAppModule.getAdMobAppContext().isTestAdUnitIdEnabled()) {
				interstitial.setAdUnitId(TEST_AD_UNIT_ID);
			} else {
				interstitial.setAdUnitId(interstitialAdUnitId);
			}

			AdRequest.Builder builder = createBuilder();
			interstitial.loadAd(builder.build());

			AdListenerWrapper adListenerWrapper = new AdListenerWrapper();
			adListenerWrapper.addAdListener(new AdListener() {

				@Override
				public void onAdLoaded() {
					if (displayInterstitial) {
						displayInterstitial(false);
					}
				}

				@Override
				public void onAdOpened() {
					AdsStats.INSTANCE.onInterstitialOpened();
				}
			});
			if (adListeners != null) {
				for (AdListener adListener : adListeners) {
					adListenerWrapper.addAdListener(adListener);
				}
			}
			interstitial.setAdListener(adListenerWrapper);
		}
	}

	public Boolean displayInterstitial() {
		return displayInterstitial(false);
	}

	public Boolean displayInterstitial(Boolean retryIfNotLoaded) {
		return displayInterstitial(retryIfNotLoaded, null);
	}

	public Boolean displayInterstitial(Boolean retryIfNotLoaded, AdListener adListener) {
		displayInterstitial = retryIfNotLoaded;
		if ((interstitial != null) && interstitial.isLoaded()) {
			if (adListener != null) {
				((AdListenerWrapper)interstitial.getAdListener()).addAdListener(adListener);
			}
			interstitial.show();
			displayInterstitial = false;
			return true;
		}
		return false;
	}

	@Override
	public AdHelper setAdUnitId(String adUnitId) {
		this.interstitialAdUnitId = adUnitId;
		return this;
	}

	@Override
	public AdHelper setAdListeners(List<AdListener> adListeners) {
		this.adListeners = adListeners;
		return this;
	}
}
