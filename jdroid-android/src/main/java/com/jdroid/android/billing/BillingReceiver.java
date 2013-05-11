package com.jdroid.android.billing;

import org.slf4j.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.jdroid.java.utils.LoggerUtils;

/**
 * This class implements the broadcast receiver for in-app billing. All asynchronous messages from the Google Play app
 * come to this app through this receiver. This class forwards all messages to the {@link BillingService}, which can
 * start background threads, if necessary, to process the messages. This class runs on the UI thread and must not do any
 * network I/O, database updates, or any tasks that might take a long time to complete. It also must not start a
 * background thread because that may be killed as soon as {@link #onReceive(Context, Intent)} returns.
 */
public class BillingReceiver extends BroadcastReceiver {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(BillingReceiver.class);
	
	// Intent actions that we receive from the Google Play app
	public static final String ACTION_NOTIFY = "com.android.vending.billing.IN_APP_NOTIFY";
	public static final String ACTION_RESPONSE_CODE = "com.android.vending.billing.RESPONSE_CODE";
	public static final String ACTION_PURCHASE_STATE_CHANGED = "com.android.vending.billing.PURCHASE_STATE_CHANGED";
	
	/**
	 * This is the entry point for all asynchronous messages sent from the Google Play app to the application. This
	 * method forwards the messages on to the {@link BillingService}, which handles the communication back to the Google
	 * Play app.
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (ACTION_PURCHASE_STATE_CHANGED.equals(action)) {
			String signedData = intent.getStringExtra(BillingService.INAPP_SIGNED_DATA);
			String signature = intent.getStringExtra(BillingService.INAPP_SIGNATURE);
			purchaseStateChanged(context, signedData, signature);
		} else if (ACTION_NOTIFY.equals(action)) {
			String notificationId = intent.getStringExtra(BillingService.NOTIFICATION_ID);
			handleNotify(context, notificationId);
		} else if (ACTION_RESPONSE_CODE.equals(action)) {
			long requestId = intent.getLongExtra(BillingService.INAPP_REQUEST_ID, -1);
			checkResponseCode(context, requestId, BillingResponseCode.valueOf(intent));
		} else {
			LOGGER.warn("Unexpected action: " + action);
		}
	}
	
	/**
	 * This is called when the Google Play app sends information about a purchase state change. The signedData parameter
	 * is a plaintext JSON string that is signed by the server with the developer's private key. The signature for the
	 * signed data is passed in the signature parameter.
	 * 
	 * @param context the context
	 * @param signedData the signed JSON string (signed, not encrypted)
	 * @param signature the signature for the data, signed with the private key
	 */
	private void purchaseStateChanged(Context context, String signedData, String signature) {
		Intent intent = new Intent(ACTION_PURCHASE_STATE_CHANGED);
		intent.setClass(context, BillingService.class);
		intent.putExtra(BillingService.INAPP_SIGNED_DATA, signedData);
		intent.putExtra(BillingService.INAPP_SIGNATURE, signature);
		context.startService(intent);
	}
	
	/**
	 * This is called when the Google Play app sends a "notify" message indicating that transaction information is
	 * available. The request includes a nonce (random number used once) that we generate and the Google Play app signs
	 * and sends back to us with the purchase state and other transaction details. This BroadcastReceiver cannot bind to
	 * the MarketBillingService directly so it starts the {@link BillingService}, which does the actual work of sending
	 * the message.
	 * 
	 * @param context the context
	 * @param notifyId the notification ID
	 */
	private void handleNotify(Context context, String notificationId) {
		LOGGER.debug("Notification id: " + notificationId);
		Intent intent = new Intent(BillingService.ACTION_GET_PURCHASE_INFORMATION);
		intent.setClass(context, BillingService.class);
		intent.putExtra(BillingService.NOTIFICATION_ID, notificationId);
		context.startService(intent);
	}
	
	/**
	 * This is called when the Google Play app sends a server response code. The BillingService can then report the
	 * status of the response if desired.
	 * 
	 * @param context the context
	 * @param requestId the request ID that corresponds to a previous request
	 * @param responseCodeIndex the ResponseCode ordinal value for the request
	 */
	private void checkResponseCode(Context context, long requestId, BillingResponseCode responseCode) {
		Intent intent = new Intent(ACTION_RESPONSE_CODE);
		intent.setClass(context, BillingService.class);
		intent.putExtra(BillingService.INAPP_REQUEST_ID, requestId);
		intent.putExtra(BillingService.INAPP_RESPONSE_CODE, responseCode);
		context.startService(intent);
	}
}
