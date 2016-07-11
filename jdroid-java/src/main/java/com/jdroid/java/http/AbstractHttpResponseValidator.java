package com.jdroid.java.http;

import com.jdroid.java.exception.ErrorCode;
import com.jdroid.java.exception.HttpResponseException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public abstract class AbstractHttpResponseValidator extends BasicHttpResponseValidator {

	private static final Logger LOGGER = LoggerUtils.getLogger(AbstractHttpResponseValidator.class);

	private static final String STATUS_CODE_HEADER = "status-code";
	private static final String SUCCESSFUL_STATUS_CODE = "200";

	protected void onSuccess(HttpResponseWrapper httpResponse, String message) {
		ErrorCode errorCode = getErrorCode(httpResponse);
		if (errorCode != null) {
			throw errorCode.newErrorCodeException();
		}
	}

	protected void onClientError(HttpResponseWrapper httpResponse, String message) {
		ErrorCode errorCode = getErrorCode(httpResponse);
		if (errorCode != null) {
			throw errorCode.newErrorCodeException();
		} else {
			throw new HttpResponseException(message);
		}
	}

	private ErrorCode getErrorCode(HttpResponseWrapper httpResponse) {
		ErrorCode errorCode = null;
		String statusCode = httpResponse.getHeader(STATUS_CODE_HEADER);
		if (statusCode != null) {
			LOGGER.debug("Server Status code: " + statusCode);
			if (!statusCode.equals(SUCCESSFUL_STATUS_CODE)) {
				errorCode = findByStatusCode(statusCode);
				if (errorCode == null) {
					errorCode = findByCommonStatusCode(statusCode);
					if (errorCode == null) {
						LOGGER.warn("Unknown Server Status code: " + statusCode);
						throw new HttpResponseException("Unknown Server Status code: " + statusCode);
					}
				}
			}
		}
		return errorCode;
	}

	protected abstract ErrorCode findByCommonStatusCode(String statusCode);

	protected abstract ErrorCode findByStatusCode(String statusCode);
}