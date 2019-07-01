package com.jdroid.android.exception;

import com.jdroid.android.R;
import com.jdroid.java.exception.ErrorCode;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.java.utils.ValidationUtils;

import java.util.Collection;

public enum CommonErrorCode implements ErrorCode {

	INVALID_CREDENTIALS(R.string.jdroid_invalidCredentials, 401),
	INVALID_USER_TOKEN(null, 402),

	// The app is not compatible with the API anymore and needs to be upgraded.
	INVALID_API_VERSION(null, 403),

	FACEBOOK_SESSION_EXPIRED_ERROR(null);

	private Integer resourceId;
	private Integer statusCode;

	CommonErrorCode(Integer resourceId, Integer statusCode) {
		this.resourceId = resourceId;
		this.statusCode = statusCode;
	}

	CommonErrorCode(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public static ErrorCode findByStatusCode(String statusCode) {
		ErrorCode errorCode = null;
		for (CommonErrorCode each : values()) {
			if ((each.statusCode != null) && each.statusCode.toString().equals(statusCode)) {
				errorCode = each;
				break;
			}
		}
		return errorCode;
	}

	@Override
	public ErrorCodeException newErrorCodeException(Object... errorCodeParameters) {
		return new ErrorCodeException(this, errorCodeParameters);
	}

	@Override
	public ErrorCodeException newErrorCodeException() {
		return new ErrorCodeException(this);
	}

	@Override
	public ErrorCodeException newErrorCodeException(Throwable throwable) {
		return new ErrorCodeException(this, throwable);
	}

	@Override
	public ErrorCodeException newErrorCodeException(String message) {
		return new ErrorCodeException(this, message);
	}

	@Override
	public String getStatusCode() {
		return statusCode.toString();
	}

	@Override
	public Integer getTitleResId() {
		return null;
	}

	@Override
	public Integer getDescriptionResId() {
		return resourceId;
	}

	public void validateRequired(String value) {
		if (StringUtils.isEmpty(value)) {
			throw newErrorCodeException();
		}
	}

	public void validateRequired(Object value) {
		if (value == null) {
			throw newErrorCodeException();
		}
	}

	public void validateRequired(Collection<?> value) {
		if ((value == null) || value.isEmpty()) {
			throw newErrorCodeException();
		}
	}

	public void validatePositive(Integer value) {
		if (value <= 0) {
			throw newErrorCodeException();
		}
	}

	public void validatePositive(Float value) {
		if (value <= 0) {
			throw newErrorCodeException();
		}
	}

	public void validateMaximum(Integer value, Integer maximum) {
		if (value > maximum) {
			throw newErrorCodeException(maximum);
		}
	}

	public void validateMinimum(int value, int minimum) {
		if (value < minimum) {
			throw newErrorCodeException(minimum);
		}
	}

	public void validateMinimumLength(String value, int minimum) {
		if (value.length() < minimum) {
			throw newErrorCodeException(minimum);
		}
	}

	public void validateMaximumLength(String value, int maximum) {
		if (value.length() > maximum) {
			throw newErrorCodeException(maximum);
		}
	}

	/**
	 * Validates that the two values are equals. Assumes that no value is null.
	 *
	 * @param value
	 * @param otherValue
	 */
	public void validateEquals(Object value, Object otherValue) {
		if ((value != null) && !value.equals(otherValue)) {
			throw newErrorCodeException();
		}
	}

	/**
	 * Validate that the value is an email address
	 *
	 * @param value
	 */
	public void validateEmail(String value) {
		if (!ValidationUtils.INSTANCE.isValidEmail(value)) {
			throw newErrorCodeException();
		}
	}
}
