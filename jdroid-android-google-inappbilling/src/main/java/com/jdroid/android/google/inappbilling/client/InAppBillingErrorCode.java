package com.jdroid.android.google.inappbilling.client;

import com.android.billingclient.api.BillingClient;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.inappbilling.R;
import com.jdroid.java.exception.ErrorCode;
import com.jdroid.java.exception.ErrorCodeException;

public enum InAppBillingErrorCode implements ErrorCode {

	FEATURE_NOT_SUPPORTED(null, BillingClient.BillingResponse.FEATURE_NOT_SUPPORTED),
	
	SERVICE_DISCONNECTED(null, BillingClient.BillingResponse.SERVICE_DISCONNECTED),
	
	// User pressed back or canceled a dialog
	USER_CANCELED(null, BillingClient.BillingResponse.USER_CANCELED),
	
	// Network connection is down
	SERVICE_UNAVAILABLE(R.string.jdroid_connectionErrorDescription, BillingClient.BillingResponse.SERVICE_UNAVAILABLE),
	
	// Indicates that in-app billing is not available because the API_VERSION that you specified is not recognized by
	// the Google Play app or the user is ineligible for in-app billing (for example, the user resides in a
	// country that prohibits in-app purchases).
	BILLING_UNAVAILABLE(R.string.jdroid_notSupportedInAppBillingError, BillingClient.BillingResponse.BILLING_UNAVAILABLE, false),
	
	// Indicates that the Google Play app cannot find the requested item in the application's product list. This can
	// happen if the product ID is misspelled in your REQUEST_PURCHASE request or if an item is unpublished in the
	// application's product list.
	ITEM_UNAVAILABLE(null, BillingClient.BillingResponse.ITEM_UNAVAILABLE),
	
	// Indicates that an application is trying to make an in-app billing request but the application has not declared
	// the com.android.vending.BILLING permission in its manifest. Can also indicate that an application is not properly
	// signed, or that you sent a malformed request, such as a request with missing Bundle keys or a request that uses
	// an unrecognized request type.
	DEVELOPER_ERROR(null, BillingClient.BillingResponse.DEVELOPER_ERROR),
	
	// Indicates an unexpected server error. For example, this error is triggered if you try to purchase an item from
	// yourself, which is not allowed by Google Checkout.
	UNEXPECTED_ERROR(null, BillingClient.BillingResponse.ERROR),
	
	// Failure to purchase since item is already owned
	ITEM_ALREADY_OWNED(null, BillingClient.BillingResponse.ITEM_ALREADY_OWNED),
	
	// Failure to consume since item is not owned
	ITEM_NOT_OWNED(null, BillingClient.BillingResponse.ITEM_NOT_OWNED),
	
	// Purchase signature verification failed
	VERIFICATION_FAILED(null),
	
	// Missing token
	MISSING_TOKEN(null),
	
	BAD_PURCHASE_DATA(null),
	
	// Subscriptions are not available.
	SUBSCRIPTIONS_NOT_AVAILABLE(null),

	// Invalid consumption attempt
	INVALID_CONSUMPTION(null);

	private Integer resourceId;
	private Integer errorResponseCode;
	private Boolean trackable = true;

	InAppBillingErrorCode(Integer resourceId, Integer errorResponseCode, Boolean trackable) {
		this.resourceId = resourceId;
		this.errorResponseCode = errorResponseCode;
		this.trackable = trackable;
	}

	InAppBillingErrorCode(Integer resourceId, Integer errorResponseCode) {
		this.resourceId = resourceId;
		this.errorResponseCode = errorResponseCode;
	}

	InAppBillingErrorCode(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public static InAppBillingErrorCode findByErrorResponseCode(Integer errorResponseCode) {
		InAppBillingErrorCode errorCode = null;
		if (errorResponseCode  != BillingClient.BillingResponse.OK) {
			for (InAppBillingErrorCode each : values()) {
				if ((each.errorResponseCode != null) && each.errorResponseCode.equals(errorResponseCode)) {
					errorCode = each;
					break;
				}
			}
			if (errorCode == null) {
				AbstractApplication.get().getExceptionHandler().logWarningException("Invalid in-app billing error response code: " + errorResponseCode);
				errorCode = UNEXPECTED_ERROR;
			}
		}
		return errorCode;
	}

	@Override
	public String getStatusCode() {
		return null;
	}

	@Override
	public ErrorCodeException newErrorCodeException(Object... errorCodeParameters) {
		return new ErrorCodeException(this, errorCodeParameters).setTrackable(trackable);
	}

	@Override
	public ErrorCodeException newErrorCodeException() {
		return new ErrorCodeException(this).setTrackable(trackable);
	}

	@Override
	public ErrorCodeException newErrorCodeException(Throwable throwable) {
		return new ErrorCodeException(this, throwable).setTrackable(trackable);
	}

	@Override
	public ErrorCodeException newErrorCodeException(String message) {
		return new ErrorCodeException(this, name() + ": " + message).setTrackable(trackable);
	}

	@Override
	public Integer getTitleResId() {
		return null;
	}

	@Override
	public Integer getDescriptionResId() {
		return resourceId;
	}
}