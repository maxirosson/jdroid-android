package com.jdroid.android.google.inappbilling.analytics;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker;
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsParams;
import com.jdroid.android.google.inappbilling.client.Product;

public class FirebaseInAppBillingAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements InAppBillingAnalyticsTracker {

	@Override
	public void trackInAppBillingPurchaseTry(Product product) {
		FirebaseAnalyticsParams params = new FirebaseAnalyticsParams();
		params.put(FirebaseAnalytics.Param.ITEM_ID, product.getId());
		getFirebaseAnalyticsHelper().sendEvent("in_app_purchase_try", params);
	}

	@Override
	public void trackInAppBillingPurchase(Product product) {
		// Tracked automatically by Firebase
	}
}
