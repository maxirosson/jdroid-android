package com.jdroid.android.billing;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.android.vending.billing.IMarketBillingService;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class BillingManagerImpl implements BillingManager, ServiceConnection {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(BillingManagerImpl.class);
	
	// This is the action we use to bind to the MarketBillingService.
	public static final String MARKET_BILLING_SERVICE_ACTION = "com.android.vending.billing.MarketBillingService.BIND";
	
	// The service connection to the remote MarketBillingService.
	private IMarketBillingService service;
	
	// The list of requests that are pending while we are waiting for the connection to the MarketBillingService to be
	// established.
	private LinkedList<BillingRequest> pendingRequests = new LinkedList<BillingRequest>();
	
	// The list of requests that we have sent to the Google Play app but for which we have not yet received a response
	// code. The Map is indexed by the request Id that each request receives when it executes.
	private Map<Long, BillingRequest> sentRequests = Maps.newHashMap();
	
	private Context context = AbstractApplication.get();
	
	/**
	 * @see com.jdroid.android.billing.BillingManager#register(com.jdroid.android.billing.BillingListener)
	 */
	@Override
	public synchronized void register(BillingListener billingListener) {
		BillingResponseHandler.register(billingListener);
	}
	
	/**
	 * @see com.jdroid.android.billing.BillingManager#unregister(com.jdroid.android.billing.BillingListener)
	 */
	@Override
	public synchronized void unregister(BillingListener billingListener) {
		BillingResponseHandler.unregister(billingListener);
	}
	
	/**
	 * @see com.jdroid.android.billing.BillingManager#register(com.jdroid.android.billing.PurchaseObserver)
	 */
	@Override
	public synchronized void register(PurchaseObserver purchaseObserver) {
		BillingResponseHandler.register(purchaseObserver);
	}
	
	/**
	 * @see com.jdroid.android.billing.BillingManager#unregister(com.jdroid.android.billing.PurchaseObserver)
	 */
	@Override
	public synchronized void unregister(PurchaseObserver purchaseObserver) {
		BillingResponseHandler.unregister(purchaseObserver);
	}
	
	/**
	 * The base class for all requests that use the MarketBillingService. Each derived class overrides the run() method
	 * to call the appropriate service interface. If we are already connected to the MarketBillingService, then we call
	 * the run() method directly. Otherwise, we bind to the service and save the request on a queue to be run later when
	 * the service is connected.
	 */
	abstract class BillingRequest {
		
		public static final String BILLING_RESPONSE_REQUEST_ID = "REQUEST_ID";
		public static final String BILLING_RESPONSE_RESPONSE_CODE = "RESPONSE_CODE";
		public static final long BILLING_RESPONSE_INVALID_REQUEST_ID = -1;
		
		// These are the names of the fields in the request bundle.
		private static final String BILLING_REQUEST_METHOD = "BILLING_REQUEST";
		private static final String BILLING_REQUEST_API_VERSION = "API_VERSION";
		private static final String BILLING_REQUEST_PACKAGE_NAME = "PACKAGE_NAME";
		public static final String BILLING_REQUEST_NOTIFY_IDS = "NOTIFY_IDS";
		public static final String BILLING_REQUEST_NONCE = "NONCE";
		
		private long requestId;
		
		/**
		 * Execute the request, starting the connection if necessary.
		 * 
		 * @return true if the request was executed or queued; false if there was an error starting the connection
		 */
		public boolean execute() {
			if (runIfConnected()) {
				return true;
			}
			
			if (bindToMarketBillingService()) {
				// Add a pending request to run when the service is connected.
				pendingRequests.add(this);
				return true;
			}
			return false;
		}
		
		/**
		 * The derived class must implement this method.
		 * 
		 * @throws RemoteException
		 */
		protected abstract long doExecute() throws RemoteException;
		
		/**
		 * Try running the request directly if the service is already connected.
		 * 
		 * @return true if the request ran successfully; false if the service is not connected or there was an error
		 *         when trying to use it
		 */
		public boolean runIfConnected() {
			if (service != null) {
				try {
					requestId = doExecute();
					LOGGER.debug("BillingRequest id: " + requestId);
					if (requestId >= 0) {
						sentRequests.put(requestId, this);
					}
					return true;
				} catch (RemoteException e) {
					onRemoteException(e);
				}
			}
			return false;
		}
		
		/**
		 * Called when a remote exception occurs while trying to execute the {@link #doExecute()} method. The derived
		 * class can override this to execute exception-handling code.
		 * 
		 * @param e the exception
		 */
		protected void onRemoteException(RemoteException e) {
			LOGGER.warn("Remote billing service crashed");
			service = null;
		}
		
		/**
		 * This is called when the Google Play app sends a response code for this request.
		 * 
		 * @param responseCode the response code
		 */
		protected void responseCodeReceived(BillingResponseCode responseCode) {
			// Do Nothing by default
		}
		
		protected Bundle makeRequestBundle(String method) {
			Bundle request = new Bundle();
			request.putString(BILLING_REQUEST_METHOD, method);
			request.putInt(BILLING_REQUEST_API_VERSION, 1);
			request.putString(BILLING_REQUEST_PACKAGE_NAME, AndroidUtils.getPackageName());
			return request;
		}
		
		protected void logResponseCode(String request, Bundle response) {
			BillingResponseCode responseCode = BillingResponseCode.valueOf(response);
			LOGGER.debug(request + " response code: " + responseCode);
		}
	}
	
	/**
	 * This request verifies that the Google Play app supports in-app billing.
	 */
	private class CheckBillingSupported extends BillingRequest {
		
		private static final String CHECK_BILLING_SUPPORTED = "CHECK_BILLING_SUPPORTED";
		
		/**
		 * @see com.splatt.android.common.billing.BillingService.BillingRequest#doExecute()
		 */
		@Override
		protected long doExecute() throws RemoteException {
			
			LOGGER.debug("Checking if billing is supported");
			
			Bundle requestBundle = makeRequestBundle(CHECK_BILLING_SUPPORTED);
			Bundle responseBundle = service.sendBillingRequest(requestBundle);
			BillingResponseCode responseCode = BillingResponseCode.valueOf(responseBundle);
			LOGGER.debug("CheckBillingSupported response code: " + responseCode);
			BillingResponseHandler.checkBillingSupportedResponse(responseCode);
			return BILLING_RESPONSE_INVALID_REQUEST_ID;
		}
	}
	
	/**
	 * This request sends a purchase message to the Google Play app and is the foundation of in-app billing. You send
	 * this request when a user indicates that he or she wants to purchase an item in your application. The Google Play
	 * app then handles the financial transaction by displaying the checkout user interface.
	 */
	public class RequestPurchase extends BillingRequest {
		
		private static final String REQUEST_PURCHASE = "REQUEST_PURCHASE";
		private static final String BILLING_REQUEST_ITEM_ID = "ITEM_ID";
		private static final String BILLING_REQUEST_DEVELOPER_PAYLOAD = "DEVELOPER_PAYLOAD";
		private static final String BILLING_RESPONSE_PURCHASE_INTENT = "PURCHASE_INTENT";
		
		private Purchasable purchasable;
		
		private String developerPayload;
		
		public RequestPurchase(Purchasable purchasable) {
			this(purchasable, null);
		}
		
		public RequestPurchase(Purchasable purchasable, String developerPayload) {
			this.purchasable = purchasable;
			this.developerPayload = developerPayload;
		}
		
		@Override
		protected long doExecute() throws RemoteException {
			
			LOGGER.debug("Requesting purchase for product id: " + purchasable.getProductId());
			
			Bundle request = makeRequestBundle(REQUEST_PURCHASE);
			request.putString(BILLING_REQUEST_ITEM_ID, purchasable.getProductId());
			// Note that the developer payload is optional.
			if (developerPayload != null) {
				request.putString(BILLING_REQUEST_DEVELOPER_PAYLOAD, developerPayload);
			}
			Bundle response = service.sendBillingRequest(request);
			PendingIntent pendingIntent = response.getParcelable(BILLING_RESPONSE_PURCHASE_INTENT);
			if (pendingIntent == null) {
				LOGGER.error("Error with requestPurchase for product id: " + purchasable.getProductId());
				return BILLING_RESPONSE_INVALID_REQUEST_ID;
			}
			
			BillingResponseHandler.startMarketCheckoutActivity(pendingIntent, new Intent());
			return response.getLong(BILLING_RESPONSE_REQUEST_ID, BILLING_RESPONSE_INVALID_REQUEST_ID);
		}
		
		@Override
		protected void responseCodeReceived(BillingResponseCode responseCode) {
			BillingResponseHandler.responseCodeReceived(this, responseCode);
		}
	}
	
	/**
	 * This request acknowledges that your application received the details of a purchase state change. The Google Play
	 * app sends purchase state change notifications to your application until you confirm that you received them.
	 */
	class ConfirmNotifications extends BillingRequest {
		
		private static final String CONFIRM_NOTIFICATIONS = "CONFIRM_NOTIFICATIONS";
		
		private final String[] notifyIds;
		
		public ConfirmNotifications(String[] notifyIds) {
			this.notifyIds = notifyIds;
		}
		
		@Override
		protected long doExecute() throws RemoteException {
			
			LOGGER.debug("Confirming notifications for notification ids: " + notifyIds);
			
			Bundle request = makeRequestBundle(CONFIRM_NOTIFICATIONS);
			request.putStringArray(BILLING_REQUEST_NOTIFY_IDS, notifyIds);
			Bundle response = service.sendBillingRequest(request);
			logResponseCode("ConfirmNotifications", response);
			return response.getLong(BILLING_RESPONSE_REQUEST_ID, BILLING_RESPONSE_INVALID_REQUEST_ID);
		}
	}
	
	/**
	 * This request retrieves the details of a purchase state change. A purchase changes state when a requested purchase
	 * is billed successfully or when a user cancels a transaction during checkout. It can also occur when a previous
	 * purchase is refunded. The Google Play app notifies your application when a purchase changes state, so you only
	 * need to send this request when there is transaction information to retrieve.
	 */
	class GetPurchaseInformation extends BillingRequest {
		
		private static final String GET_PURCHASE_INFORMATION = "GET_PURCHASE_INFORMATION";
		
		private long nonce;
		
		private final String[] notifyIds;
		
		public GetPurchaseInformation(String[] notifyIds) {
			this.notifyIds = notifyIds;
		}
		
		@Override
		protected long doExecute() throws RemoteException {
			
			LOGGER.debug("Getting purchase information for notification ids: " + notifyIds);
			
			nonce = Security.generateNonce();
			
			Bundle request = makeRequestBundle(GET_PURCHASE_INFORMATION);
			request.putLong(BILLING_REQUEST_NONCE, nonce);
			request.putStringArray(BILLING_REQUEST_NOTIFY_IDS, notifyIds);
			Bundle response = service.sendBillingRequest(request);
			logResponseCode("GetPurchaseInformation", response);
			return response.getLong(BILLING_RESPONSE_REQUEST_ID, BILLING_RESPONSE_INVALID_REQUEST_ID);
		}
		
		@Override
		protected void onRemoteException(RemoteException e) {
			super.onRemoteException(e);
			Security.removeNonce(nonce);
		}
	}
	
	/**
	 * This request retrieves a user's transaction status for managed purchases. You should send this request only when
	 * you need to retrieve a user's transaction status, which is usually only when your application is reinstalled or
	 * installed for the first time on a device.
	 */
	public class RestoreTransactions extends BillingRequest {
		
		private static final String RESTORE_TRANSACTIONS = "RESTORE_TRANSACTIONS";
		
		private long nonce;
		
		@Override
		protected long doExecute() throws RemoteException {
			
			LOGGER.debug("Executing restore transactions");
			
			nonce = Security.generateNonce();
			
			Bundle request = makeRequestBundle(RESTORE_TRANSACTIONS);
			request.putLong(BILLING_REQUEST_NONCE, nonce);
			Bundle response = service.sendBillingRequest(request);
			logResponseCode("RestoreTransactions", response);
			return response.getLong(BILLING_RESPONSE_REQUEST_ID, BILLING_RESPONSE_INVALID_REQUEST_ID);
		}
		
		@Override
		protected void onRemoteException(RemoteException e) {
			super.onRemoteException(e);
			Security.removeNonce(nonce);
		}
		
		@Override
		protected void responseCodeReceived(BillingResponseCode responseCode) {
			BillingResponseHandler.responseCodeReceived(this, responseCode);
		}
	}
	
	/**
	 * @see com.jdroid.android.billing.BillingManager#checkResponseCode(long,
	 *      com.jdroid.android.billing.BillingResponseCode)
	 */
	@Override
	public void checkResponseCode(long requestId, BillingResponseCode responseCode) {
		BillingRequest request = sentRequests.get(requestId);
		if (request != null) {
			LOGGER.debug(request.getClass().getSimpleName() + " response code: " + responseCode);
			request.responseCodeReceived(responseCode);
		}
		sentRequests.remove(requestId);
	}
	
	/**
	 * Binds to the MarketBillingService and returns true if the bind succeeded.
	 * 
	 * @return true if the bind succeeded; false otherwise
	 */
	private boolean bindToMarketBillingService() {
		try {
			LOGGER.debug("Binding to Market billing service");
			boolean bindResult = false;
			if (context != null) {
				bindResult = context.bindService(new Intent(MARKET_BILLING_SERVICE_ACTION), this,
					Context.BIND_AUTO_CREATE);
			}
			if (bindResult) {
				LOGGER.debug("Market billing service bind successful.");
				return true;
			} else {
				LOGGER.error("Could not bind to Market billing service.");
			}
		} catch (SecurityException e) {
			LOGGER.error("Security exception: " + e);
		}
		return false;
	}
	
	/**
	 * @see com.jdroid.android.billing.BillingManager#checkBillingSupported()
	 */
	@Override
	public boolean checkBillingSupported() {
		return new CheckBillingSupported().execute();
	}
	
	/**
	 * @see com.jdroid.android.billing.BillingManager#requestPurchase(com.jdroid.android.billing.Purchasable,
	 *      java.lang.String)
	 */
	@Override
	public boolean requestPurchase(Purchasable purchasable, String developerPayload) {
		return new RequestPurchase(purchasable, developerPayload).execute();
	}
	
	/**
	 * @see com.jdroid.android.billing.BillingManager#restoreTransactions()
	 */
	@Override
	public boolean restoreTransactions() {
		return new RestoreTransactions().execute();
	}
	
	/**
	 * @see com.jdroid.android.billing.BillingManager#confirmNotifications(java.lang.String[])
	 */
	@Override
	public boolean confirmNotifications(String... notifyIds) {
		return new ConfirmNotifications(notifyIds).execute();
	}
	
	/**
	 * @see com.jdroid.android.billing.BillingManager#getPurchaseInformation(java.lang.String[])
	 */
	@Override
	public boolean getPurchaseInformation(String[] notifyIds) {
		return new GetPurchaseInformation(notifyIds).execute();
	}
	
	/**
	 * @see com.jdroid.android.billing.BillingManager#purchaseStateChanged(java.lang.String, java.lang.String)
	 */
	@Override
	public void purchaseStateChanged(String signedData, String signature) {
		List<PurchaseOrder> orders = Security.verifyPurchase(signedData, signature);
		if ((orders != null) && !orders.isEmpty()) {
			BillingResponseHandler.notifyPurchaseStateChanged(orders);
		}
	}
	
	/**
	 * This is called when we are connected to the MarketBillingService. This runs in the main UI thread.
	 * 
	 * @see android.content.ServiceConnection#onServiceConnected(android.content.ComponentName, android.os.IBinder)
	 */
	@Override
	public void onServiceConnected(ComponentName name, IBinder binder) {
		LOGGER.debug("MarketBillingService connected");
		service = IMarketBillingService.Stub.asInterface(binder);
		runPendingRequests();
	}
	
	/**
	 * Runs any pending requests that are waiting for a connection to the service to be established. This runs in the
	 * main UI thread.
	 */
	private void runPendingRequests() {
		BillingRequest request;
		while ((request = pendingRequests.peek()) != null) {
			if (request.runIfConnected()) {
				// Remove the request
				pendingRequests.remove();
			} else {
				// The service crashed, so restart it. Note that this leaves the current request on the queue.
				bindToMarketBillingService();
				return;
			}
		}
	}
	
	/**
	 * This is called when we are disconnected from the MarketBillingService.
	 * 
	 * @see android.content.ServiceConnection#onServiceDisconnected(android.content.ComponentName)
	 */
	@Override
	public void onServiceDisconnected(ComponentName name) {
		LOGGER.warn("Billing service disconnected");
		service = null;
	}
	
	/**
	 * @see com.jdroid.android.billing.BillingManager#bind(android.content.Context)
	 */
	@Override
	public void bind(Context context) {
		this.context = context;
	}
	
	/**
	 * @see com.jdroid.android.billing.BillingManager#unbind()
	 */
	@Override
	public void unbind() {
		try {
			context.unbindService(this);
			context = null;
		} catch (IllegalArgumentException e) {
			// This might happen if the service was disconnected
		}
	}
	
}
