package com.jdroid.android.facebook;

import org.slf4j.Logger;
import android.os.Bundle;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.exception.CommonErrorCode;
import com.jdroid.java.utils.LoggerUtils;

/**
 * TODO FB
 * 
 * @author Estefania Caravatti
 */
public class DefaultFacebookDialogListener implements DialogListener {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(DefaultFacebookDialogListener.class);
	
	/**
	 * @see com.facebook.android.Facebook.DialogListener#onComplete(android.os.Bundle)
	 */
	@Override
	public void onComplete(Bundle values) {
		LOGGER.debug("Facebook connection completed.");
	}
	
	/**
	 * @see com.facebook.android.Facebook.DialogListener#onFacebookError(com.facebook.android.FacebookError)
	 */
	@Override
	public void onFacebookError(FacebookError e) {
		LOGGER.debug("Facebook error while connecting.", e);
		AbstractApplication.get().getExceptionHandler().handleException(Thread.currentThread(),
			CommonErrorCode.FACEBOOK_ERROR.newApplicationException(e));
	}
	
	/**
	 * @see com.facebook.android.Facebook.DialogListener#onError(com.facebook.android.DialogError)
	 */
	@Override
	public void onError(DialogError e) {
		LOGGER.debug("Error while connecting.", e);
		AbstractApplication.get().getExceptionHandler().handleException(Thread.currentThread(),
			CommonErrorCode.FACEBOOK_ERROR.newApplicationException(e));
	}
	
	/**
	 * @see com.facebook.android.Facebook.DialogListener#onCancel()
	 */
	@Override
	public void onCancel() {
		LOGGER.debug("Connection to Facebook has been canceled.");
	}
	
}
