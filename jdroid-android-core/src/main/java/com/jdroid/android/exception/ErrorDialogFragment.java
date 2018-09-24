package com.jdroid.android.exception;

import android.content.DialogInterface;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.jdroid.android.R;
import com.jdroid.android.dialog.AlertDialogFragment;

public class ErrorDialogFragment extends AlertDialogFragment {

	private static final String ERROR_DIALOG_STRATEGY_EXTRA = "errorDialogStrategyExtra";

	public static void show(FragmentActivity activity, String title, String message, boolean goBackOnError) {
		DefaultErrorDialogStrategy defaultErrorDialogStrategy = new DefaultErrorDialogStrategy();
		defaultErrorDialogStrategy.setGoBackOnError(goBackOnError);
		show(activity, title, message, defaultErrorDialogStrategy);
	}

	public static void show(FragmentActivity activity, String title, String message,
							ErrorDialogStrategy errorDialogStrategy) {

		// This code is intentionally left out of the "if" statement to consume time before the call to
		// "findFragmentByTag" and minimize the possibility of showing the dialog twice
		ErrorDialogFragment fragment = new ErrorDialogFragment();
		fragment.addParameter(ERROR_DIALOG_STRATEGY_EXTRA, errorDialogStrategy);

		String okButton = activity.getString(R.string.jdroid_ok);
		String dialogTag = generateDialogTag(title, message, errorDialogStrategy);
		Fragment currentErrorDialogFragment = activity.getSupportFragmentManager().findFragmentByTag(dialogTag);
		if (currentErrorDialogFragment == null) {
			AlertDialogFragment.show(activity, fragment, null, title, message, null, null, okButton, true, null, dialogTag);
		}
	}

	private static String generateDialogTag(String title, String message, ErrorDialogStrategy errorDialogStrategy) {
		StringBuilder builder = new StringBuilder();
		if (title != null) {
			builder.append(title);
		}
		if (message != null) {
			builder.append(message);
		}
		if (errorDialogStrategy != null) {
			builder.append(errorDialogStrategy.getClass().getSimpleName());
		}
		return String.valueOf(builder.toString().hashCode());
	}

	/**
	 * @see com.jdroid.android.dialog.AlertDialogFragment#onPositiveClick()
	 */
	@Override
	protected void onPositiveClick() {
		handleStrategy();
	}

	/**
	 * @see DialogFragment#onCancel(android.content.DialogInterface)
	 */
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		handleStrategy();
	}

	private void handleStrategy() {
		((ErrorDialogStrategy)getArgument(ERROR_DIALOG_STRATEGY_EXTRA)).onPositiveClick(getActivity());
	}
}
