package com.jdroid.android.gcm;

import org.slf4j.Logger;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

/**
 * Base {@link IntentService} to handle Google Cloud Messaging (GCM) messages.
 * 
 * You need to add the following permissions to your manifest:
 * 
 * <pre>
 * &lt;permission android:name="[app package].permission.C2D_MESSAGE" android:protectionLevel="signature" />
 * &lt;uses-permission android:name="[app package].permission.C2D_MESSAGE" />
 * &lt;uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
 * &lt;uses-permission android:name="android.permission.WAKE_LOCK" />
 * &lt;uses-permission android:name="android.permission.GET_ACCOUNTS" />
 * </pre>
 */
public class GcmService extends IntentService {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(GcmService.class);
	
	// wakelock
	private static PowerManager.WakeLock sWakeLock;
	
	// Java lock used to synchronize access to sWakelock
	private static final Object LOCK = GcmService.class;
	
	/**
	 * Constructor that does not set a sender id, useful when the sender id is context-specific.
	 */
	public GcmService() {
		super(GcmService.class.getSimpleName());
	}
	
	/**
	 * Called when a cloud message has been received.
	 * 
	 * @param context application's context.
	 * @param intent intent containing the message payload as extras.
	 */
	public void onMessage(Context context, Intent intent) {
		GcmMessage gcmMessage = AbstractApplication.get().getGcmResolver().resolve(intent);
		if (gcmMessage != null) {
			gcmMessage.handle(intent);
		}
	}
	
	/**
	 * Called to indicate that the server deleted some pending messages because they were collapsible.
	 * 
	 * @param context application's context.
	 */
	protected void onDeletedMessages(Context context, Intent intent) {
	}
	
	/**
	 * 
	 * @param context application's context.
	 */
	protected void onError(Context context, Intent intent) {
	}
	
	@Override
	public final void onHandleIntent(Intent intent) {
		try {
			Context context = getApplicationContext();
			
			GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
			String messageType = gcm.getMessageType(intent);
			
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				onError(context, intent);
				LOGGER.trace("Send error");
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				LOGGER.trace("Received deleted messages");
				onDeletedMessages(context, intent);
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				LOGGER.trace("Received message");
				onMessage(context, intent);
			}
			
		} finally {
			// Release the power lock, so phone can get back to sleep.
			// The lock is reference-counted by default, so multiple
			// messages are ok.
			
			// If onMessage() needs to spawn a thread or do something else,
			// it should use its own lock.
			synchronized (LOCK) {
				// sanity check for null as this is a public method
				if (sWakeLock != null) {
					LOGGER.trace("Releasing wakelock");
					sWakeLock.release();
				}
			}
		}
	}
	
	/**
	 * Called from the broadcast receiver.
	 * <p>
	 * Will process the received intent, call handleMessage(), registered(), etc. in background threads, with a wake
	 * lock, while keeping the service alive.
	 */
	static void runIntentInService(Context context, Intent intent, String className) {
		synchronized (LOCK) {
			if (sWakeLock == null) {
				// This is called from BroadcastReceiver, there is no init.
				PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
				sWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, GcmService.class.getSimpleName());
			}
		}
		LOGGER.trace("Acquiring wakelock");
		sWakeLock.acquire();
		intent.setClassName(context, className);
		context.startService(intent);
	}
	
}
