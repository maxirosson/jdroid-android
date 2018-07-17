package com.jdroid.android.exception;

import android.support.v4.app.FragmentActivity;

import com.jdroid.java.exception.AbstractException;

public class DialogErrorDisplayer extends AbstractErrorDisplayer {

	private static final String GO_BACK_KEY = "goBack";
	private static final String ERROR_DIALOG_STRATEGY_KEY = "errorDialogStrategy";

	private Boolean goBackOnErrorByDefault;

	public DialogErrorDisplayer() {
		this(true);
	}

	public DialogErrorDisplayer(Boolean goBackOnErrorByDefault) {
		this.goBackOnErrorByDefault = goBackOnErrorByDefault;
	}

	@Override
	public void onDisplayError(FragmentActivity activity, String title, String description, Throwable throwable) {
		if (activity != null) {
			ErrorDialogFragment.show(activity, title, description, getErrorDialogStrategy(throwable));
		}
	}

	private ErrorDialogStrategy getErrorDialogStrategy(Throwable throwable) {
		if (throwable instanceof AbstractException) {
			AbstractException abstractException = (AbstractException)throwable;
			return abstractException.hasParameter(ERROR_DIALOG_STRATEGY_KEY) ? abstractException.<ErrorDialogStrategy>getParameter(ERROR_DIALOG_STRATEGY_KEY)
					: getDefaultErrorDialogStrategy(abstractException);
		}
		return getDefaultErrorDialogStrategy(throwable);
	}

	private DefaultErrorDialogStrategy getDefaultErrorDialogStrategy(Throwable throwable) {
		DefaultErrorDialogStrategy defaultStrategy = new DefaultErrorDialogStrategy();
		if (throwable instanceof AbstractException) {
			AbstractException abstractException = (AbstractException)throwable;
			defaultStrategy.setGoBackOnError(abstractException.hasParameter(GO_BACK_KEY) ? abstractException.<Boolean>getParameter(GO_BACK_KEY)
					: goBackOnErrorByDefault(abstractException));
		} else {
			defaultStrategy.setGoBackOnError(goBackOnErrorByDefault(throwable));
		}
		return defaultStrategy;
	}

	public static void setErrorDialogStrategy(AbstractException abstractException, ErrorDialogStrategy strategy) {
		abstractException.addParameter(ERROR_DIALOG_STRATEGY_KEY, strategy);
	}

	protected Boolean goBackOnErrorByDefault(Throwable throwable) {
		return goBackOnErrorByDefault;
	}

	public static void markAsGoBackOnError(AbstractException abstractException) {
		abstractException.addParameter(GO_BACK_KEY, true);
	}

	public static void markAsNotGoBackOnError(AbstractException abstractException) {
		abstractException.addParameter(GO_BACK_KEY, false);
	}


}
