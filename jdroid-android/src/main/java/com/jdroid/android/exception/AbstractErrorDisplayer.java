package com.jdroid.android.exception;

import com.jdroid.android.R;
import com.jdroid.android.google.GooglePlayUtils;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.exception.ErrorCodeException;

public abstract class AbstractErrorDisplayer implements ErrorDisplayer {

	private static final String ERROR_DISPLAYER = "errorDisplayer";

	@Override
	public final void displayError(Throwable throwable) {

		if (DefaultExceptionHandler.matchAnyErrorCode(throwable, CommonErrorCode.INVALID_API_VERSION)) {
			handleInvalidApiVersionError();
		} else {
			String title = null;
			String description = null;
			if (throwable instanceof AbstractException) {
				AbstractException abstractException = (AbstractException)throwable;
				title = abstractException.getTitle();
				description = abstractException.getDescription();
				if (((title == null) || (description == null)) && (abstractException instanceof ErrorCodeException)) {
					ErrorCodeException errorCodeException = (ErrorCodeException)abstractException;
					if (title == null) {
						title = LocalizationUtils.getTitle(errorCodeException.getErrorCode());
						if ((title == null)
								&& errorCodeException.getErrorCode().equals(
								com.jdroid.java.exception.CommonErrorCode.CONNECTION_ERROR)) {
							title = LocalizationUtils.getString(R.string.jdroid_connectionErrorTitle);
						}
					}
					if (description == null) {
						description = LocalizationUtils.getDescription(errorCodeException.getErrorCode(),
								errorCodeException.getErrorCodeDescriptionArgs());
						if ((description == null)
								&& errorCodeException.getErrorCode().equals(
								com.jdroid.java.exception.CommonErrorCode.CONNECTION_ERROR)) {
							description = LocalizationUtils.getString(R.string.jdroid_connectionErrorDescription);
						}
					}
				}
			}

			if (title == null) {
				title = LocalizationUtils.getString(R.string.jdroid_defaultErrorTitle, AppUtils.getApplicationName());
			}

			if (description == null) {
				description = LocalizationUtils.getString(R.string.jdroid_defaultErrorDescription);
			}

			onDisplayError(title, description, throwable);
		}
	}

	protected void handleInvalidApiVersionError() {
		GooglePlayUtils.showUpdateDialog();
	}

	protected abstract void onDisplayError(String title, String description, Throwable throwable);

	public static void setErrorDisplayer(AbstractException abstractException, ErrorDisplayer errorDisplayer) {
		abstractException.addParameter(ERROR_DISPLAYER, errorDisplayer);
	}

	public static ErrorDisplayer getErrorDisplayer(Throwable throwable) {
		ErrorDisplayer errorDisplayer = null;
		if (throwable instanceof AbstractException) {
			errorDisplayer = ((AbstractException)throwable).getParameter(ERROR_DISPLAYER);
		}
		if (errorDisplayer == null) {
			errorDisplayer = new DialogErrorDisplayer();
		}
		return errorDisplayer;
	}
}
