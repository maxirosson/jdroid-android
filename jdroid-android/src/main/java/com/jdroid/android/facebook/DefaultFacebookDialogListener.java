package com.jdroid.android.facebook;

import android.os.Bundle;
import android.util.Log;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.exception.CommonErrorCode;

/**
 * 
 * @author Estefania Caravatti
 */
public class DefaultFacebookDialogListener implements DialogListener {
	
	private static final String TAG = DefaultFacebookDialogListener.class.getSimpleName();
	
	/**
	 * @see com.facebook.android.Facebook.DialogListener#onComplete(android.os.Bundle)
	 */
	@Override
	public void onComplete(Bundle values) {
		Log.d(TAG, "Facebook connection completed.");
	}
	
	/**
	 * @see com.facebook.android.Facebook.DialogListener#onFacebookError(com.facebook.android.FacebookError)
	 */
	@Override
	public void onFacebookError(FacebookError e) {
		Log.d(TAG, "Facebook error while connecting.", e);
		AbstractApplication.get().getExceptionHandler().handleException(Thread.currentThread(),
			CommonErrorCode.FACEBOOK_ERROR.newApplicationException(e));
	}
	
	/**
	 * @see com.facebook.android.Facebook.DialogListener#onError(com.facebook.android.DialogError)
	 */
	@Override
	public void onError(DialogError e) {
		Log.d(TAG, "Error while connecting.", e);
		AbstractApplication.get().getExceptionHandler().handleException(Thread.currentThread(),
			CommonErrorCode.FACEBOOK_ERROR.newApplicationException(e));
	}
	
	/**
	 * @see com.facebook.android.Facebook.DialogListener#onCancel()
	 */
	@Override
	public void onCancel() {
		Log.d(TAG, "Connection to Facebook has been canceled.");
	}
	
}
