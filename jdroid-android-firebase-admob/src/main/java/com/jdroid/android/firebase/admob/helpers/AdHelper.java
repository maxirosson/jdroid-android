package com.jdroid.android.firebase.admob.helpers;

import android.app.Activity;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;

import java.util.List;

public interface AdHelper {
	
	AdHelper setAdUnitId(String adUnitId);
	
	AdHelper setAdListeners(List<AdListener> adListeners);

	void loadAd(Activity activity, ViewGroup adViewContainer);
}
