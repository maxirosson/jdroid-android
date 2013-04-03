package com.jdroid.android.exception;

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
			AlertDialogFragment.show(activity, fragment, title, message, okButton, null, true);
		}
	}
	
	/**
	 * @see com.jdroid.android.dialog.AlertDialogFragment#onPostivieClick()
	 */
	@Override
	protected void onPostivieClick() {
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
			getActivity().finish();
		}
	}
}
