package com.jdroid.android.billing;

import java.util.List;
import org.slf4j.Logger;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import com.jdroid.android.billing.BillingManagerImpl.RequestPurchase;
import com.jdroid.android.billing.BillingManagerImpl.RestoreTransactions;
import com.jdroid.java.utils.LoggerUtils;

/**
 * This class contains the methods that handle responses from the Google Play app. An application might also want to
 * forward some responses on to its own server.
 */
public class BillingResponseHandler {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(BillingResponseHandler.class);
	
	// {@link PurchaseObserver} that the application creates and registers to listen responses from the Google Play app.
	private static PurchaseObserver purchaseObserver;
	private static BillingListener billingListener;
	
	/**
	 * Registers an observer to listen events
	 * 
	 * @param observer the observer to register
	 */
	public static synchronized void register(PurchaseObserver observer) {
		purchaseObserver = observer;
	}
	
	/**
	 * Unregisters a previously registered observer.
	 * 
	 * @param observer the previously registered observer.
	 */
	public static synchronized void unregister(PurchaseObserver observer) {
		if (observer.equals(purchaseObserver)) {
			purchaseObserver = null;
		}
	}
	
	public static synchronized void register(BillingListener listener) {
		billingListener = listener;
	}
	
	public static synchronized void unregister(BillingListener listener) {
		if (listener.equals(billingListener)) {
			billingListener = null;
		}
	}
	
	/**
	 * Notifies the application of the availability of the MarketBillingService. This method is called in response to
	 * the application calling {@link BillingManager#checkBillingSupported()}.
	 * 
	 * @param responseCode a response code from the Google Play app to indicate the state of the request
	 */
	public static void checkBillingSupportedResponse(BillingResponseCode responseCode) {
		if (billingListener != null) {
			
			if (responseCode.equals(BillingResponseCode.RESULT_OK)) {
				LOGGER.debug("In App Billing is supported");
				billingListener.onBillingSupported();
			} else if (responseCode.equals(BillingResponseCode.RESULT_BILLING_UNAVAILABLE)) {
				LOGGER.debug("In App Billing is not supported");
				billingListener.onBillingNotSupported();
			} else if (responseCode.equals(BillingResponseCode.RESULT_ERROR)) {
				LOGGER.debug("There was an error connecting with the Google Play app.");
				billingListener.onGooglePlayConnectionError();
			} else if (responseCode.equals(BillingResponseCode.RESULT_DEVELOPER_ERROR)) {
				LOGGER.debug("Developer error when trying to make an in-app billing request");
			}
		}
	}
	
	/**
	 * Starts a new activity for the user to buy an item for sale. This method forwards the intent on to the
	 * PurchaseObserver (if it exists) because we need to start the activity on the activity stack of the application.
	 * 
	 * @param pendingIntent a PendingIntent that we received from the Google Play app that will create the new buy page
	 *            activity
	 * @param intent an intent containing a request id in an extra field that will be passed to the buy page activity
	 *            when it is created
	 */
	public static void startMarketCheckoutActivity(PendingIntent pendingIntent, Intent intent) {
		if (purchaseObserver != null) {
			try {
				purchaseObserver.getActivity().startIntentSender(pendingIntent.getIntentSender(), intent, 0, 0, 0);
			} catch (SendIntentException e) {
				LOGGER.error("Error starting activity", e);
			}
		} else {
			LOGGER.debug("Not purchase observer registered");
		}
	}
	
	/**
	 * This is called when we receive a response code from the Google Play app for a RequestPurchase request that we
	 * made. This is used for reporting various errors and also for acknowledging that an order was sent successfully to
	 * the server. This is NOT used for any purchase state changes. All purchase state changes are received in the
	 * {@link BillingReceiver} and are handled in {@link Security#verifyPurchase(String, String)}.
	 * 
	 * @param request the RequestPurchase request for which we received a response code
	 * @param responseCode a response code from the Google Play app to indicate the state of the request
	 */
	public static void responseCodeReceived(RequestPurchase request, BillingResponseCode responseCode) {
		if (purchaseObserver != null) {
			
			if (responseCode.equals(BillingResponseCode.RESULT_OK)) {
				LOGGER.debug("Purchase request was successfully sent to server");
				purchaseObserver.onRequestPurchaseOk(request);
			} else if (responseCode.equals(BillingResponseCode.RESULT_USER_CANCELED)) {
				LOGGER.debug("User canceled purchase");
				purchaseObserver.onRequestPurchaseCanceledByUser(request);
			} else if (responseCode.equals(BillingResponseCode.RESULT_SERVICE_UNAVAILABLE)) {
				LOGGER.debug("Purchase Service Unavailable");
				purchaseObserver.onRequestPurchaseServiceUnavailable(request);
			} else if (responseCode.equals(BillingResponseCode.RESULT_BILLING_UNAVAILABLE)) {
				LOGGER.debug("In-app billing is not supported yet");
				purchaseObserver.onRequestPurchaseBillingUnavailable(request);
			} else if (responseCode.equals(BillingResponseCode.RESULT_ITEM_UNAVAILABLE)) {
				LOGGER.debug("The item this app offered for sale does not exist in the server-side catalog");
				purchaseObserver.onRequestPurchaseItemUnavailable(request);
			} else if (responseCode.equals(BillingResponseCode.RESULT_ERROR)) {
				LOGGER.debug("Purchase error");
				purchaseObserver.onRequestPurchaseError(request);
			} else if (responseCode.equals(BillingResponseCode.RESULT_DEVELOPER_ERROR)) {
				LOGGER.error("Developer error when trying to make an in-app billing request");
			}
		}
	}
	
	/**
	 * This is called when we receive a response code from the Google Play app for a RestoreTransactions request.
	 * 
	 * @param request the RestoreTransactions request for which we received a response code
	 * @param responseCode a response code from the Google Play app to indicate the state of the request
	 */
	public static void responseCodeReceived(RestoreTransactions request, BillingResponseCode responseCode) {
		if (purchaseObserver != null) {
			if (responseCode == BillingResponseCode.RESULT_OK) {
				LOGGER.debug("Completed RestoreTransactions request");
				purchaseObserver.onRestoreTransactionsResponseOk(request);
			} else {
				LOGGER.debug("RestoreTransactions error: " + responseCode);
				purchaseObserver.onRestoreTransactionsResponseError(request);
			}
			
		}
	}
	
	/**
	 * Notifies the application of purchase state changes. The application can offer an item for sale to the user via
	 * {@link BillingManager#requestPurchase(Purchasable, String)}. The BillingService calls this method after it gets
	 * the response. Another way this method can be called is if the user bought something on another device running
	 * this same app. Then the Google Play app notifies the other devices that the user has purchased an item, in which
	 * case the BillingService will also call this method. Finally, this method can be called if the item was refunded.
	 * 
	 * @param orders
	 */
	public static void notifyPurchaseStateChanged(List<PurchaseOrder> orders) {
		if (billingListener != null) {
			
			for (PurchaseOrder order : orders) {
				
				if (order.getPurchaseState().equals(PurchaseState.PURCHASED)) {
					LOGGER.debug("Purchased product: " + order.toString());
					billingListener.onPurchased(order);
				}
				
				if (order.getPurchaseState().equals(PurchaseState.REFUNDED)) {
					LOGGER.debug("Refunded product: " + order.toString());
					billingListener.onRefunded(order);
				}
				
				if (order.getPurchaseState().equals(PurchaseState.CANCELED)) {
					LOGGER.debug("Canceled product: " + order.toString());
					billingListener.onCanceled(order);
				}
			}
		}
	}
}
