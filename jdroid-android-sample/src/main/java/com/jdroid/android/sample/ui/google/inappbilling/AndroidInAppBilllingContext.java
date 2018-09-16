package com.jdroid.android.sample.ui.google.inappbilling;

import com.jdroid.android.google.inappbilling.InAppBillingContext;
import com.jdroid.android.google.inappbilling.client.ProductType;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AndroidInAppBilllingContext extends InAppBillingContext {

	@Override
	public List<ProductType> getManagedProductTypes() {
		return Lists.newArrayList(SampleProductType.values());
	}
}
