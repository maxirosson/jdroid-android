package com.jdroid.android.firebase.admob.helpers;

import android.app.Activity;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.admob.AdMobAppModule;
import com.jdroid.android.firebase.admob.HouseAdBuilder;
import com.jdroid.android.location.LocationHelper;
import com.jdroid.android.utils.AppUtils;

import java.util.List;

public abstract class BaseAdViewHelper implements AdHelper {

	public static final String TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";

	private BaseAdViewWrapper baseAdViewWrapper;
	private ViewGroup adViewContainer;
	private AdSize adSize;
	private String adUnitId;
	private HouseAdBuilder houseAdBuilder;

	private List<AdListener> adListeners;

	private Boolean displayAds = false;

	public BaseAdViewHelper() {
		adUnitId = AdMobAppModule.getAdMobAppContext().getDefaultAdUnitId();
	}

	@Override
	public void loadAd(Activity activity, final ViewGroup adViewContainer) {
		if (adViewContainer != null) {
			if ((getAdSize() != null) && AdMobAppModule.getAdMobAppContext().areAdsEnabled()) {
				if (adUnitId != null) {

					baseAdViewWrapper = createBaseAdViewWrapper(activity);

					if (!AppUtils.isReleaseBuildType() && AdMobAppModule.getAdMobAppContext().isTestAdUnitIdEnabled()) {
						baseAdViewWrapper.setAdUnitId(BaseAdViewHelper.TEST_AD_UNIT_ID);
					} else {
						baseAdViewWrapper.setAdUnitId(adUnitId);
					}
					baseAdViewWrapper.setAdSize(getAdSize());
					final View customView = getHouseAdBuilder() != null ? getHouseAdBuilder().build(activity) : null;

					AdListenerWrapper adListenerWrapper = new AdListenerWrapper();
					if (customView != null) {

						adViewContainer.setVisibility(View.VISIBLE);
						adViewContainer.addView(customView);

						adListenerWrapper.addAdListener(new AdListener() {

							@Override
							public void onAdLoaded() {
								if (displayAds) {
									baseAdViewWrapper.getBaseAdView().setVisibility(View.VISIBLE);
									customView.setVisibility(View.GONE);
								} else {
									baseAdViewWrapper.getBaseAdView().postDelayed(new Runnable() {

										@Override
										public void run() {
											displayAds = true;
											if ((baseAdViewWrapper != null) && (customView != null)) {
												baseAdViewWrapper.getBaseAdView().setVisibility(View.VISIBLE);
												customView.setVisibility(View.GONE);
											}
										}
									}, DateUtils.SECOND_IN_MILLIS * 10);
								}
							}

							@Override
							public void onAdClosed() {
								baseAdViewWrapper.getBaseAdView().setVisibility(View.GONE);
								customView.setVisibility(View.VISIBLE);
							}

							@Override
							public void onAdFailedToLoad(int errorCode) {
								baseAdViewWrapper.getBaseAdView().setVisibility(View.GONE);
								customView.setVisibility(View.VISIBLE);
							}
						});
					} else {
						adListenerWrapper.addAdListener(new AdListener() {

							@Override
							public void onAdLoaded() {
								adViewContainer.setVisibility(View.VISIBLE);
								baseAdViewWrapper.getBaseAdView().setVisibility(View.VISIBLE);
							}

							@Override
							public void onAdClosed() {
								adViewContainer.setVisibility(View.GONE);
								baseAdViewWrapper.getBaseAdView().setVisibility(View.GONE);
							}

							@Override
							public void onAdFailedToLoad(int errorCode) {
								adViewContainer.setVisibility(View.GONE);
								baseAdViewWrapper.getBaseAdView().setVisibility(View.GONE);
							}
						});
					}

					if (adListeners != null) {
						for (AdListener adListener : adListeners) {
							adListenerWrapper.addAdListener(adListener);
						}
					}
					baseAdViewWrapper.setAdListener(adListenerWrapper);

					AdRequest.Builder builder = createBuilder();
					try {
						baseAdViewWrapper.loadAd(builder.build());
						adViewContainer.addView(baseAdViewWrapper.getBaseAdView());
					} catch (Exception e) {
						AbstractApplication.get().getExceptionHandler().logWarningException("Error when loading ad", e);
					}

				} else {
					AbstractApplication.get().getExceptionHandler().logWarningException("Missing ad unit ID on activity " + activity.getClass().getSimpleName());
				}
			} else {
				adViewContainer.setVisibility(View.GONE);
			}
		}
	}

	protected abstract BaseAdViewWrapper createBaseAdViewWrapper(Activity activity);

	protected AdRequest.Builder createBuilder() {
		final AdRequest.Builder builder = new AdRequest.Builder();
		if (!AppUtils.isReleaseBuildType()) {
			builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
			for (String deviceId : AdMobAppModule.getAdMobAppContext().getTestDevicesIds()) {
				builder.addTestDevice(deviceId);
			}
		}
		builder.setLocation(LocationHelper.get().getLocation());
		return builder;
	}

	public AdHelper setAdSize(AdSize adSize) {
		this.adSize = adSize;
		return this;
	}

	public AdSize getAdSize() {
		return adSize;
	}

	public AdHelper setHouseAdBuilder(HouseAdBuilder houseAdBuilder) {
		this.houseAdBuilder = houseAdBuilder;
		return this;
	}

	public HouseAdBuilder getHouseAdBuilder() {
		return houseAdBuilder;
	}

	@Override
	public AdHelper setAdUnitId(String adUnitId) {
		this.adUnitId = adUnitId;
		return this;
	}

	@Override
	public AdHelper setAdListeners(List<AdListener> adListeners) {
		this.adListeners = adListeners;
		return this;
	}

	public void onBeforePause() {
		if (baseAdViewWrapper != null) {
			baseAdViewWrapper.pause();
		}
	}

	public void onResume() {
		if (baseAdViewWrapper != null) {
			if (AdMobAppModule.getAdMobAppContext().areAdsEnabled()) {
				baseAdViewWrapper.resume();
			} else if (adViewContainer != null) {
				adViewContainer.removeView(baseAdViewWrapper.getBaseAdView());
				adViewContainer.setVisibility(View.GONE);
				baseAdViewWrapper = null;
				adViewContainer = null;
			}
		}

	}

	public void onBeforeDestroy() {
		if (baseAdViewWrapper != null) {
			baseAdViewWrapper.destroy();
			baseAdViewWrapper = null;
			adViewContainer = null;
		}
	}
}
