package com.jdroid.android.firebase.admob.helpers;

import com.google.android.gms.ads.AdListener;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

public class AdListenerWrapper extends AdListener {

	private Logger LOGGER = LoggerUtils.getLogger(AdListenerWrapper.class);

	private List<AdListener> wrappedAdListeners = Lists.INSTANCE.newArrayList();
	private List<AdListener> transientAdListeners = Lists.INSTANCE.newArrayList();

	@Override
	public void onAdLoaded() {
		LOGGER.info("Ad loaded");
		for (AdListener adListener : wrappedAdListeners) {
			adListener.onAdLoaded();
		}
	}

	@Override
	public void onAdFailedToLoad(int i) {
		LOGGER.info("Ad failed to load: " + i);
		for (AdListener adListener : wrappedAdListeners) {
			adListener.onAdFailedToLoad(i);
		}
	}

	@Override
	public void onAdOpened() {
		LOGGER.info("Ad opened");
		for (AdListener adListener : wrappedAdListeners) {
			adListener.onAdOpened();
		}
	}

	@Override
	public void onAdImpression() {
		LOGGER.info("Ad impression");
		for (AdListener adListener : wrappedAdListeners) {
			adListener.onAdImpression();
		}
	}

	@Override
	public void onAdClicked() {
		LOGGER.info("Ad clicked");
		for (AdListener adListener : wrappedAdListeners) {
			adListener.onAdClicked();
		}
	}

	@Override
	public void onAdClosed() {
		LOGGER.info("Ad closed");
		for (AdListener adListener : wrappedAdListeners) {
			adListener.onAdClosed();
			transientAdListeners.remove(adListener);
		}
	}

	@Override
	public void onAdLeftApplication() {
		LOGGER.info("Ad left application");
		for (AdListener adListener : wrappedAdListeners) {
			adListener.onAdLeftApplication();
		}
	}

	public void addAdListener(AdListener adListener) {
		this.wrappedAdListeners.add(adListener);
	}

	public void addTransientAdListener(AdListener adListener) {
		this.wrappedAdListeners.add(adListener);
		this.transientAdListeners.add(adListener);
	}
}
