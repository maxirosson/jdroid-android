package com.jdroid.android.sample.ui.google.inappbilling;

import com.jdroid.android.google.inappbilling.client.Product;
import com.jdroid.android.google.inappbilling.ui.InAppBillingRecyclerFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.ToastUtils;

public class GoogleInAppBillingFragment extends InAppBillingRecyclerFragment {

	@Override
	public void onPurchased(final Product product) {
		getAdapter().notifyDataSetChanged();
		ToastUtils.showToast(R.string.jdroid_purchaseThanks);
	}

	@Override
	public void onProvideProduct(Product product) {
		// Do nothing
	}
}
