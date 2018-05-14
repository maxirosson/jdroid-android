package com.jdroid.android.firebase.admob;


import com.jdroid.java.date.DateUtils;

public class AdsStats {
	
	private static Long lastInterstitialOpenedTimestamp = 0L;
	
	public static void onInterstitialOpened() {
		lastInterstitialOpenedTimestamp = DateUtils.nowMillis();
	}
	
	public static Long getLastInterstitialOpenedTimestamp() {
		return lastInterstitialOpenedTimestamp;
	}
}
