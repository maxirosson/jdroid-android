package com.jdroid.android.google.inappbilling.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.inappbilling.client.Product;

public abstract class InAppBillingFragment extends AbstractFragment implements InAppBillingListener {
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		if (savedInstanceState == null) {
			InAppBillingHelperFragment.add(getActivity(), InAppBillingHelperFragment.class, false, this);
		}
	}
	
	public void launchPurchaseFlow(Product product) {
		InAppBillingHelperFragment inAppBillingHelperFragment = InAppBillingHelperFragment.get(getActivity());
		if (inAppBillingHelperFragment != null) {
			inAppBillingHelperFragment.launchPurchaseFlow(product);
		}
	}
	
	@Override
	public void onConsumed(Product product) {
		// Do Nothing
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		InAppBillingHelperFragment inAppBillingHelperFragment = InAppBillingHelperFragment.get(getActivity());
		if (inAppBillingHelperFragment != null) {
			inAppBillingHelperFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		InAppBillingHelperFragment.removeTarget(getActivity());
	}
}
