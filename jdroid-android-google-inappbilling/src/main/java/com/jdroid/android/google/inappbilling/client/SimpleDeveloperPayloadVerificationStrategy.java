package com.jdroid.android.google.inappbilling.client;

public class SimpleDeveloperPayloadVerificationStrategy implements DeveloperPayloadVerificationStrategy {

	@Override
	public Boolean verify(Product product) {
		/*
		 * TODO verify that the developer payload of the purchase is correct. It will be the same one that you sent
		 * when initiating the purchase. WARNING: Locally generating a random string when starting a purchase and
		 * verifying it here might seem like a good approach, but this will fail in the case where the user purchases an
		 * item on one device and then uses your app on a different device, because on the other device you will not
		 * have access to the random string you originally generated. So a good developer payload has these
		 * characteristics: 1. If two different users purchase an item, the payload is different between them, so that
		 * one user's purchase can't be replayed to another user. 2. The payload must be such that you can verify it
		 * even when the app wasn't the one who initiated the purchase flow (so that items purchased by the user on one
		 * device work on other devices owned by the user). Using your own server to store and verify developer payloads
		 * across app installations is recommended.
		 */

		return product.getDeveloperPayload().equals(product.getPurchase().getDeveloperPayload());
	}

}
