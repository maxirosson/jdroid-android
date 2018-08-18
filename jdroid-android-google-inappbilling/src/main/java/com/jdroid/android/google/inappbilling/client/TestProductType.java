package com.jdroid.android.google.inappbilling.client;

public class TestProductType {

	// When you make an in-app billing request with this product ID, the Google Play app responds as though you
	// successfully purchased an item. The response includes a JSON string, which contains fake purchase information
	// (for example, a fake order ID). In some cases, the JSON string is signed and the response includes the signature
	// so you can test your signature verification implementation using these responses.
	public static final String PURCHASED = "android.test.purchased";

	// When you make an in-app billing request with this product ID the Google Play app responds as though the purchase
	// was canceled. This can occur when an error is encountered in the order process, such as an invalid credit card,
	// or when you cancel a user's order before it is charged.
	public static final String CANCELED = "android.test.canceled";

	// When you make an in-app billing request with this product ID, the Google Play app responds as though the purchase
	// was refunded. Refunds cannot be initiated through the Google Play app's in-app billing service. Refunds must be
	// initiated by you (the merchant). After you process a refund request through your Google Checkout account, a
	// refund message is sent to your application by the Google Play app. This occurs only when the Google Play app gets
	// notification from Google Checkout that a refund has been made. For more information about refunds, see Handling
	// IN_APP_NOTIFY messages and In-app Billing Pricing.
	public static final String REFUNDED = "android.test.refunded";

	// When you make an in-app billing request with this product ID, the Google Play app responds as though the item
	// being purchased was not listed in your application's product list.
	public static final String UNAVAILABLE = "android.test.item_unavailable";
}
