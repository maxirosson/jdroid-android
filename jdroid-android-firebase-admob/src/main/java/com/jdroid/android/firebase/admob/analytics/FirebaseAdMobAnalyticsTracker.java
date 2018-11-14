package com.jdroid.android.firebase.admob.analytics;

import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker;

public class FirebaseAdMobAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements AdMobAnalyticsTracker {

	@Override
	public void trackRemoveAdsBannerClicked() {
		getFirebaseAnalyticsHelper().sendEvent("removed_ads");
	}
}
