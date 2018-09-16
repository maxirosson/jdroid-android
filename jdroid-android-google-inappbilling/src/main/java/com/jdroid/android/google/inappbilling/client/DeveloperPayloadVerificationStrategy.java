package com.jdroid.android.google.inappbilling.client;

public interface DeveloperPayloadVerificationStrategy {

	/*
	 *  Verifies the developer payload of a purchase.
	 */
	public Boolean verify(Product product);
}
