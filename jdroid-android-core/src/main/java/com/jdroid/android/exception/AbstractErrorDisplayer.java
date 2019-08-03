package com.jdroid.android.exception;

import androidx.fragment.app.FragmentActivity;

import com.jdroid.android.R;
import com.jdroid.android.google.GooglePlayUtils;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.http.exception.HttpErrorCode;

public abstract class AbstractErrorDisplayer implements ErrorDisplayer {

	private static final String ERROR_DISPLAYER = "errorDisplayer";

	@Override
	public final void displayError(FragmentActivity activity, Throwable throwable) {

		if (DefaultExceptionHandler.matchAnyErrorCode(throwable, CommonErrorCode.INVALID_API_VERSION)) {
			handleInvalidApiVersionError(activity);
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
						title = LocalizationUtils.INSTANCE.getTitle(errorCodeException.getErrorCode());
						if ((title == null)
							&& errorCodeException.getErrorCode().equals(HttpErrorCode.CONNECTION_ERROR)) {
							title = LocalizationUtils.INSTANCE.getString(R.string.jdroid_connectionErrorTitle);
						}
					}
					if (description == null) {
						description = LocalizationUtils.INSTANCE.getDescription(errorCodeException.getErrorCode(),
							errorCodeException.getErrorCodeDescriptionArgs());
						if ((description == null)
							&& errorCodeException.getErrorCode().equals(HttpErrorCode.CONNECTION_ERROR)) {
							description = LocalizationUtils.INSTANCE.getString(R.string.jdroid_connectionErrorDescription);
						}
					}
				}
			}

			if (title == null) {
				title = LocalizationUtils.INSTANCE.getString(R.string.jdroid_defaultErrorTitle, AppUtils.INSTANCE.getApplicationName());
			}

			if (description == null) {
				description = LocalizationUtils.INSTANCE.getString(R.string.jdroid_defaultErrorDescription);
			}

			onDisplayError(activity, title, description, throwable);
		}
	}

	protected void handleInvalidApiVersionError(FragmentActivity activity) {
		GooglePlayUtils.INSTANCE.showUpdateDialog(activity);
	}

	protected abstract void onDisplayError(FragmentActivity activity, String title, String description, Throwable throwable);

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
