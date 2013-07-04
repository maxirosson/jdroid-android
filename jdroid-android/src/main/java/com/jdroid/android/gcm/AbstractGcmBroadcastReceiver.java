package com.jdroid.android.gcm;

import org.slf4j.Logger;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.jdroid.java.utils.LoggerUtils;

/**
 * {@link BroadcastReceiver} that receives GCM messages and delivers them to an application-specific
 * {@link AbstractGcmService} subclass.
 * 
 * @author Maxi Rosson
 */
public abstract class AbstractGcmBroadcastReceiver extends BroadcastReceiver {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(AbstractGcmBroadcastReceiver.class);
	
	@Override
	public final void onReceive(Context context, Intent intent) {
		
		LOGGER.trace("onReceive: " + intent.getAction());
		
		// Delegates to the application-specific intent service.
		String className = getGCMIntentServiceClassName(context);
		AbstractGcmService.runIntentInService(context, intent, className);
		
		setResult(Activity.RESULT_OK, null, null);
	}
	
	/**
	 * Gets the class name of the intent service that will handle GCM messages.
	 */
	protected abstract String getGCMIntentServiceClassName(Context context);
}
