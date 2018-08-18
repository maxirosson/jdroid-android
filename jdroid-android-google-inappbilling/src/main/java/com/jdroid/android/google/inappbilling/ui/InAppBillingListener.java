package com.jdroid.android.google.inappbilling.ui;

import android.support.annotation.MainThread;

import com.jdroid.android.google.inappbilling.client.Product;

import java.util.List;

public interface InAppBillingListener {

	/**
	 * This method is executed (on the UI thread) when the products are loaded
	 *
	 * @param products The loaded products
	 */
	@MainThread
	public void onProductsLoaded(List<Product> products);

	/**
	 * This method is executed (on the UI thread) when the product was purchased
	 *
	 * @param product The purchased {@link Product}
	 */
	@MainThread
	public void onPurchased(Product product);

	/**
	 * This method is executed (on the UI thread) when the product was consumed
	 *
	 * @param product The consumed {@link Product}
	 */
	@MainThread
	public void onConsumed(Product product);

	@MainThread
	public void onProvideProduct(Product product);
}
