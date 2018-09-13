package com.jdroid.android.debug.crash;

import com.jdroid.java.exception.CommonErrorCode;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.exception.ConnectionException;
import com.jdroid.java.http.exception.HttpResponseException;

public enum ExceptionType {

	CONNECTION_EXCEPTION {
		@Override
		public void crash() {
			throw new ConnectionException(CRASH_MESSAGE);
		}
	},
	ERROR_CODE_EXCEPTION {
		@Override
		public void crash() {
			throw new ErrorCodeException(CommonErrorCode.UNEXPECTED_ERROR, CRASH_MESSAGE);
		}
	},
	HTTP_RESPONSE_EXCEPTION {
		@Override
		public void crash() {
			throw new HttpResponseException(CRASH_MESSAGE);
		}
	},
	UNEXPECTED_EXCEPTION {
		@Override
		public void crash() {
			throw new UnexpectedException(CRASH_MESSAGE);
		}
	},
	UNEXPECTED_WRAPPED_EXCEPTION {
		@SuppressWarnings({ "null", "ConstantConditions", "ResultOfMethodCallIgnored" })
		@Override
		public void crash() {
			try {
				Long value = null;
				value.toString();
			} catch (Exception e) {
				throw new UnexpectedException(CRASH_MESSAGE, e);
			}
		}
	},
	UNEXPECTED_NO_MESSAGE_WRAPPED_EXCEPTION {
		@SuppressWarnings({ "null", "ConstantConditions", "ResultOfMethodCallIgnored" })
		@Override
		public void crash() {
			try {
				Long value = null;
				value.toString();
			} catch (Exception e) {
				throw new UnexpectedException(e);
			}
		}
	},
	RUNTIME_EXCEPTION {
		@Override
		public void crash() {
			throw new RuntimeException(CRASH_MESSAGE);
		}
	},
	RUNTIME_NO_MESSAGE_EXCEPTION {
		@Override
		public void crash() {
			throw new RuntimeException();
		}
	};

	private static final String CRASH_MESSAGE = "This is a generated crash for testing";

	public abstract void crash();

	public static ExceptionType find(String name) {
		try {
			return ExceptionType.valueOf(name);
		} catch (Exception e) {
			return null;
		}
	}

}
