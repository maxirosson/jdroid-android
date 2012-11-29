package com.jdroid.android.ad;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.mediation.admob.AdMobAdapterExtras;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.context.DefaultApplicationContext;

/**
 * 
 * @author Maxi Rosson
 */
public class AdLoader {
	
	public static void loadAd(Activity activity, ViewGroup adViewContainer, AdSize adSize) {
		// Ads Management
		DefaultApplicationContext applicationContext = AbstractApplication.get().getAndroidApplicationContext();
		if (adViewContainer != null) {
			if ((adSize == null) || !applicationContext.areAdsEnabled()) {
				adViewContainer.setVisibility(View.GONE);
			} else {
				
				AdView adView = new AdView(activity, adSize, applicationContext.getAdUnitId());
				AdRequest adRequest = new AdRequest();
				AdMobAdapterExtras extras = new AdMobAdapterExtras();
				
				// Background color
				extras.addExtra("color_bg", "AAAAFF");
				// Gradient background color at top
				extras.addExtra("color_bg_top", "FFFFFF");
				// Border color
				extras.addExtra("color_border", "FFFFFF");
				// Link text color
				extras.addExtra("color_link", "000080");
				// Text color
				extras.addExtra("color_text", "808080");
				// URL color
				extras.addExtra("color_url", "008000");
				adRequest.setNetworkExtras(extras);
				
				if (!applicationContext.isProductionEnvironment()) {
					adRequest.setTestDevices(applicationContext.getTestDevicesIds());
					adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
				}
				adView.setAdListener(new ZoomAdListener(adView));
				adView.loadAd(adRequest);
				adViewContainer.addView(adView);
			}
		}
	}
	
}
