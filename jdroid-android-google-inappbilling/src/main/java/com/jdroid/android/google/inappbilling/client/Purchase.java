/*
 * Copyright (c) 2012 Google Inc. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package com.jdroid.android.google.inappbilling.client;


import com.jdroid.android.google.inappbilling.client.utils.Security;
import com.jdroid.java.json.JSONException;
import com.jdroid.java.json.JSONObject;
import com.jdroid.java.utils.StringUtils;

/**
 * Represents an in-app billing purchase.
 */
public class Purchase {

	public enum PurchaseState {
		PURCHASED(0),
		CANCELED(1),
		REFUNDED(2);

		private int code;

		PurchaseState(int code) {
			this.code = code;
		}

		public static PurchaseState valueOf(int code) {
			PurchaseState state = PurchaseState.PURCHASED;
			for (PurchaseState each : values()) {
				if (each.code == code) {
					return each;
				}
			}
			return state;
		}
	}

	private String orderId;
	private String productId;
	private String packageName;
	private Long purchaseTime;
	private PurchaseState state;
	private String developerPayload;
	private String token;
	private Boolean autoRenewing;
	private String signatureBase64;
	private String signature;
	private Boolean verified;

	public Purchase(String purchaseJson, String signatureBase64, String signature) throws JSONException {
		JSONObject jsonObject = new JSONObject(purchaseJson);

		// If the order is a test purchase made through the In-app Billing Sandbox, orderId is blank.
		orderId = jsonObject.optString("orderId");
		if (StringUtils.isBlank(orderId)) {
			orderId = "testOrderId";
		}

		productId = jsonObject.optString("productId");
		packageName = jsonObject.optString("packageName");
		purchaseTime = jsonObject.optLong("purchaseTime");
		state = PurchaseState.valueOf(jsonObject.optInt("purchaseState"));
		developerPayload = jsonObject.optString("developerPayload");
		token = jsonObject.optString("token", jsonObject.optString("purchaseToken"));
		autoRenewing = jsonObject.optBoolean("autoRenewing");

		this.signatureBase64 = signatureBase64;
		this.signature = signature;
		verified = false;
	}

	/*
	 * A unique order identifier for the transaction. This identifier corresponds to the Google payments order ID.
	 * If the order is a test purchase made through the In-app Billing Sandbox, orderId is "testOrderId".
	 */
	public String getOrderId() {
		return orderId;
	}

	/*
	 * The item's product identifier. Every item has a product ID, which you must specify in the application's product list on the Google Play Console.
	 */
	public String getProductId() {
		return productId;
	}

	/*
	 * The purchase state of the order.
	 */
	public PurchaseState getState() {
		return state;
	}

	/*
	 * A developer-specified string that contains supplemental information about an order.
	 * You can specify a value for this field when you make a getBuyIntent request.
	 */
	public String getDeveloperPayload() {
		return developerPayload;
	}

	/*
	 * A token that uniquely identifies a purchase for a given item and user pair.
	 */
	public String getToken() {
		return token;
	}

	public String getSignature() {
		return signature;
	}

	public void verify(Product product, String purchaseJson, DeveloperPayloadVerificationStrategy developerPayloadVerificationStrategy) {
		// TODO Perform signature verification tasks on a server
		if (productId.startsWith("android.test") || Security.verifyPurchase(signatureBase64, purchaseJson, signature)) {
			if (!developerPayloadVerificationStrategy.verify(product)) {
				throw InAppBillingErrorCode.VERIFICATION_FAILED.newErrorCodeException("Purchase developer payload verification FAILED. " + purchaseJson);
			}
		} else {
			throw InAppBillingErrorCode.VERIFICATION_FAILED.newErrorCodeException("Purchase signature verification FAILED. " + purchaseJson);
		}

		verified = true;
	}

	public Boolean isVerified() {
		return verified;
	}

	/*
	 * The time the product was purchased, in milliseconds since the epoch (Jan 1, 1970).
	 */
	public Long getPurchaseTime() {
		return purchaseTime;
	}

	/*
	 * Indicates whether the subscription renews automatically.
	 * If true, the subscription is active, and will automatically renew on the next billing date.
	 * If false, indicates that the user has canceled the subscription.
	 */
	public Boolean isAutoRenewing() {
		return autoRenewing;
	}

	/*
	 * The application package from which the purchase originated.
	 */
	public String getPackageName() {
		return packageName;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Purchase{");
		sb.append("orderId='").append(orderId).append('\'');
		sb.append(", productId='").append(productId).append('\'');
		sb.append(", purchaseTime=").append(purchaseTime);
		sb.append(", state=").append(state);
		sb.append(", developerPayload='").append(developerPayload).append('\'');
		sb.append(", token='").append(token).append('\'');
		sb.append(", signature='").append(signature).append('\'');
		sb.append(", verified=").append(verified);
		sb.append(", autoRenewing=").append(autoRenewing);
		sb.append('}');
		return sb.toString();
	}
}
