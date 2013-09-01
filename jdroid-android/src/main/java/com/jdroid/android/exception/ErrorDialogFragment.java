package com.jdroid.android.exception;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.jdroid.android.R;
import com.jdroid.android.dialog.AlertDialogFragment;

/**
 * 
 * @author Maxi Rosson
 */
public class ErrorDialogFragment extends AlertDialogFragment {
	
	private static final String SHOULD_GO_BACK_EXTRA = "shouldGoBackExtra";
	
	public static void show(FragmentActivity activity, String title, String message, Boolean shouldGoBack) {
		
		Fragment currentErrorDialogFragment = activity.getSupportFragmentManager().findFragmentByTag(
			ErrorDialogFragment.class.getSimpleName());
		
		if (currentErrorDialogFragment == null) {
			ErrorDialogFragment fragment = new ErrorDialogFragment();
			fragment.addParameter(SHOULD_GO_BACK_EXTRA, shouldGoBack);
			
			String okButton = activity.getString(R.string.ok);
			AlertDialogFragment.show(activity, fragment, title, message, null, null, okButton, true);
		}
	}
	
	/**
	 * @see com.jdroid.android.dialog.AlertDialogFragment#onPositiveClick()
	 */
	@Override
	protected void onPositiveClick() {
		goBackIfrequired();
	}
	
	/**
	 * @see android.support.v4.app.DialogFragment#onCancel(android.content.DialogInterface)
	 */
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		goBackIfrequired();
	}
	
	private void goBackIfrequired() {
		Boolean shouldGoBack = getArgument(SHOULD_GO_BACK_EXTRA);
		if (shouldGoBack) {
			Activity activiy = getActivity();
			if (activiy != null) {
				activiy.finish();
			}
		}
	}
}
