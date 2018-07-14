package com.jdroid.android.google.inappbilling.client;

import com.jdroid.android.google.inappbilling.R;
import com.jdroid.java.exception.ErrorCode;
import com.jdroid.java.exception.ErrorCodeException;

public enum InAppBillingErrorCode implements ErrorCode {
	
	// User pressed back or canceled a dialog
	USER_CANCELED(null, 1),
	
	// Network connection is down
	SERVICE_UNAVAILABLE(R.string.jdroid_connectionErrorDescription, 2),
	
	// Indicates that in-app billing is not available because the API_VERSION that you specified is not recognized by
	// the Google Play app or the user is ineligible for in-app billing (for example, the user resides in a
	// country that prohibits in-app purchases).
	BILLING_UNAVAILABLE(R.string.jdroid_notSupportedInAppBillingError, 3, false),
	
	// Indicates that the Google Play app cannot find the requested item in the application's product list. This can
	// happen if the product ID is misspelled in your REQUEST_PURCHASE request or if an item is unpublished in the
	// application's product list.
	ITEM_UNAVAILABLE(null, 4),
	
	// Indicates that an application is trying to make an in-app billing request but the application has not declared
	// the com.android.vending.BILLING permission in its manifest. Can also indicate that an application is not properly
	// signed, or that you sent a malformed request, such as a request with missing Bundle keys or a request that uses
	// an unrecognized request type.
	DEVELOPER_ERROR(null, 5),
	
	// Indicates an unexpected server error. For example, this error is triggered if you try to purchase an item from
	// yourself, which is not allowed by Google Checkout.
	UNEXPECTED_ERROR(null, 6),
	
	// Failure to purchase since item is already owned
	ITEM_ALREADY_OWNED(null, 7),
	
	// Failure to consume since item is not owned
	ITEM_NOT_OWNED(null, 8),
	
	// Remote Exception while setting up in-app billing
	REMOTE_EXCEPTION(null),
	
	// Bad response received
	BAD_RESPONSE(null),
	
	// Purchase signature verification failed
	VERIFICATION_FAILED(null),
	
	// Send intent failed
	SEND_INTENT_FAILED(null),
	
	// Missing token
	MISSING_TOKEN(null),
	
	MISSING_PURCHASE_DATA(null),
	
	BAD_PURCHASE_DATA(null),
	
	MISSING_DATA_SIGNATURE(null),
	
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
		for (InAppBillingErrorCode each : values()) {
			if ((each.errorResponseCode != null) && each.errorResponseCode.equals(errorResponseCode)) {
				errorCode = each;
				break;
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