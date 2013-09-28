package com.jdroid.android.billing;

import org.slf4j.Logger;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.utils.LoggerUtils;

/**
 * This class sends messages to the Google Play app on behalf of the application by connecting (binding) to the
 * MarketBillingService. The application creates an instance of this class and invokes billing requests through this
 * service. This service also reports state changes back to the application through the {@link BillingResponseHandler}
 * 
 * The {@link BillingReceiver} class starts this service to process commands that it receives from the Google Play app.
 */
@SuppressLint("Registered")
public class BillingService extends Service {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(BillingService.class);
	
	// Intent actions that we send from the BillingReceiver to the BillingService.
	public static final String ACTION_GET_PURCHASE_INFORMATION = AndroidUtils.getPackageName()
			+ ".GET_PURCHASE_INFORMATION";
	
	// Names of the extras that are passed in an intent from the Google Play app to this application and cannot be
	// changed.
	public static final String NOTIFICATION_ID = "notification_id";
	public static final String INAPP_SIGNED_DATA = "inapp_signed_data";
	public static final String INAPP_SIGNATURE = "inapp_signature";
	public static final String INAPP_REQUEST_ID = "request_id";
	public static final String INAPP_RESPONSE_CODE = "response_code";
	
	private BillingManager billingManager;
	
	/**
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// We don't support binding to this service, only starting the service.
		return null;
	}
	
	/**
	 * The {@link BillingReceiver} sends messages to this service using intents. Each intent has an action and some
	 * extra arguments specific to that action.
	 * 
	 * @param intent the intent containing one of the supported actions
	 * @param startId an identifier for the invocation instance of this service
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		String action = intent.getAction();
		LOGGER.debug("Handling action: " + action);
		if (ACTION_GET_PURCHASE_INFORMATION.equals(action)) {
			String notificationId = intent.getStringExtra(NOTIFICATION_ID);
			billingManager.getPurchaseInformation(new String[] { notificationId });
			stopSelf();
		} else if (BillingReceiver.ACTION_PURCHASE_STATE_CHANGED.equals(action)) {
			String signedData = intent.getStringExtra(INAPP_SIGNED_DATA);
			String signature = intent.getStringExtra(INAPP_SIGNATURE);
			billingManager.purchaseStateChanged(signedData, signature);
			stopSelf();
		} else if (BillingReceiver.ACTION_RESPONSE_CODE.equals(action)) {
			long requestId = intent.getLongExtra(INAPP_REQUEST_ID, -1);
			BillingResponseCode responseCode = (BillingResponseCode)intent.getSerializableExtra(INAPP_RESPONSE_CODE);
			billingManager.checkResponseCode(requestId, responseCode);
			stopSelf();
		} else {
			LOGGER.warn("Unexpected action: " + action);
		}
		
		stopSelf();
		return START_REDELIVER_INTENT;
	}
}
