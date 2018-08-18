package com.jdroid.android.google.inappbilling.client;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.android.vending.billing.IInAppBillingService;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.inappbilling.InAppBillingAppModule;
import com.jdroid.android.google.inappbilling.InAppBillingContext;
import com.jdroid.java.collections.CollectionUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.ErrorCode;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;

import org.json.JSONException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides convenience methods for in-app billing. You can create one instance of this class for your application and
 * use it to process in-app billing operations. It provides asynchronous (non-blocking)
 * methods for many common in-app billing operations, as well as automatic signature verification.
 * <p>
 * After instantiating, you must perform setup in order to start using the object. To perform setup, call the
 * {@link #startSetup} method and provide a listener; that listener will be notified when setup is complete, after which
 * (and not before) you may call other methods.
 * <p>
 * After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See
 * {@link #queryInventory} and related methods.
 * <p>
 * When you are done with this object, don't forget to call {@link #disposeWhenFinished()} to ensure proper cleanup. This object holds
 * a binding to the in-app billing service, which will leak unless you dispose of it correctly. If you created the
 * object on an Activity's onCreate method, then the recommended place to dispose of it is the Activity's onDestroy
 * method.
 * <p>
 * A note about threading: When using this object from a background thread, you may call the blocking versions of
 * methods; when using from a UI thread, call only the asynchronous versions and handle the results via callbacks. Also,
 * notice that you can only call one asynchronous operation at a time; attempting to start a second asynchronous
 * operation while the first one has not yet completed will result in an exception being thrown.
 */
public class InAppBillingClient {

	private final static Logger LOGGER = LoggerUtils.getLogger(InAppBillingClient.class);

	private static final int IN_APP_BILLING_API_VERSION = 3;
	private static final int IN_APP_BILLING_SUBSCRIPTION_UPDATE_API_VERSION = 5;

	// Keys for the responses from InAppBillingService
	private static final String RESPONSE_CODE = "RESPONSE_CODE";
	private static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
	private static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
	private static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
	private static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
	private static final String RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
	private static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
	private static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
	private static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";

	public static final int PURCHASE_REQUEST_CODE = 10001;

	// some fields on the getSkuDetails response bundle
	private static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";

	// Has this object been disposed of? (If so, we should ignore callbacks, etc)
	private boolean disposed = false;

	// Do we need to dispose this object after an in-progress asynchronous operation?
	private boolean disposeAfterAsync = false;

	// Are subscriptions supported?
	private Boolean subscriptionsSupported;

	// Is an asynchronous operation in progress?
	// (only one at a time can be in progress)
	private boolean asyncInProgress = false;

	// Ensure atomic access to mAsyncInProgress and mDisposeAfterAsync.
	private final Object asyncInProgressLock = new Object();

	private String asyncOperation = "";

	// Context we were passed during initialization
	private Context context;

	// Connection to the service
	private IInAppBillingService service;
	private ServiceConnection serviceConnection;

	// The request code used to launch purchase flow
	private int requestCode;

	// Public key for verifying signature, in base64 encoding
	private String signatureBase64 = null;

	// The product id used to launch the purchase flow
	private String productId;

	private InAppBillingClientListener listener;

	private Inventory inventory;

	/**
	 * Creates an instance. After creation, it will not yet be ready to use. You must perform setup by calling
	 * {@link #startSetup} and wait for setup to complete. This constructor does not block and is safe to call from a UI
	 * thread.
	 *
	 * @param context Your application or Activity context. Needed to bind to the in-app billing service.
	 */
	public InAppBillingClient(Context context) {
		this.context = context.getApplicationContext();
		signatureBase64 = InAppBillingAppModule.get().getInAppBillingContext().getGooglePlayPublicKey();
		LOGGER.debug("InAppBillingClient created.");
	}

	/**
	 * Starts the setup process. This will start up the setup process asynchronously. You will be notified through the
	 * listener when the setup process is complete.
	 */
	@MainThread
	public void startSetup(final InAppBillingClientListener listener) {
		this.listener = listener;
		try {
			LOGGER.debug("Starting in-app billing setup.");
			serviceConnection = new ServiceConnection() {

				@Override
				public void onServiceDisconnected(ComponentName name) {
					LOGGER.debug("Billing service disconnected.");
					service = null;
				}

				@Override
				public void onServiceConnected(ComponentName name, IBinder binder) {
					if (disposed) {
						return;
					}
					LOGGER.debug("Billing service connected.");
					service = IInAppBillingService.Stub.asInterface(binder);
					String packageName = context.getPackageName();
					try {
						LOGGER.debug("Checking in-app billing " + IN_APP_BILLING_API_VERSION + " support for item type " + ItemType.MANAGED);
						int response = service.isBillingSupported(IN_APP_BILLING_API_VERSION, packageName, ItemType.MANAGED.getType());
						ErrorCode inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(response);
						if (inAppBillingErrorCode == null) {

							LOGGER.debug("In-app billing supported for item type " + ItemType.MANAGED);

							LOGGER.debug("Checking in-app billing " + IN_APP_BILLING_SUBSCRIPTION_UPDATE_API_VERSION + " support for item type " + ItemType.SUBSCRIPTION);
							response = service.isBillingSupported(IN_APP_BILLING_SUBSCRIPTION_UPDATE_API_VERSION, packageName, ItemType.SUBSCRIPTION.getType());
							inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(response);
							if (inAppBillingErrorCode == null) {
								LOGGER.debug("In-app billing supported for item type " + ItemType.SUBSCRIPTION);
								subscriptionsSupported = true;
							} else {
								subscriptionsSupported = false;
								LOGGER.warn("Subscriptions NOT AVAILABLE. InAppBillingErrorCode: " + inAppBillingErrorCode);
							}

							LOGGER.debug("In-app billing setup successful.");
							if (listener != null) {
								listener.onSetupFinished();
							}

						} else {
							if (listener != null) {
								listener.onSetupFailed(inAppBillingErrorCode.newErrorCodeException());
							}
							// if in-app purchases aren't supported, neither are subscriptions.
							subscriptionsSupported = false;
						}
					} catch (RemoteException e) {
						if (listener != null) {
							listener.onSetupFailed(InAppBillingErrorCode.REMOTE_EXCEPTION.newErrorCodeException(e));
						}
					} catch (Exception e) {
						if (listener != null) {
							listener.onSetupFailed(InAppBillingErrorCode.UNEXPECTED_ERROR.newErrorCodeException(e));
						}
					}
				}
			};

			Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
			serviceIntent.setPackage("com.android.vending");
			List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentServices(serviceIntent, 0);
			if (CollectionUtils.isNotEmpty(resolveInfos)) {
				// service available to handle that Intent
				context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
			} else {
				// no service available to handle that Intent
				if (listener != null) {
					listener.onSetupFailed(InAppBillingErrorCode.BILLING_UNAVAILABLE.newErrorCodeException());
				}
			}
		} catch (Exception e) {
			if (listener != null) {
				listener.onSetupFailed(InAppBillingErrorCode.UNEXPECTED_ERROR.newErrorCodeException(e));
			}
		}
	}

	/**
	 * This will query all supported items from the server. This will do so asynchronously and call back the specified
	 * listener upon completion.
	 *
	 * @param managedProductTypes the managed {@link ProductType}s supported by the app
	 * @param subscriptionsProductTypes the subscriptions {@link ProductType}s supported by the app
	 */
	@MainThread
	public void queryInventory(final List<ProductType> managedProductTypes, final List<ProductType> subscriptionsProductTypes) {
		final Handler handler = new Handler();
		if (flagStartAsync("queryInventory")) {
			ExecutorUtils.execute(new Runnable() {

				@Override
				public void run() {
					try {
						if (!disposed) {
							inventory = new Inventory();
							if (!managedProductTypes.isEmpty()) {
								queryProductsDetails(inventory, ItemType.MANAGED, managedProductTypes);
								queryPurchases(inventory, ItemType.MANAGED);
							}

							// if subscriptions are supported, then also query for subscriptions
							if (subscriptionsSupported && !subscriptionsProductTypes.isEmpty()) {
								queryProductsDetails(inventory, ItemType.SUBSCRIPTION, subscriptionsProductTypes);
								queryPurchases(inventory, ItemType.SUBSCRIPTION);
							}

							LOGGER.debug("Query inventory was successful.");

							InAppBillingAppModule.get().getInAppBillingContext().setPurchasedProductTypes(inventory);

							handler.post(new Runnable() {
								@Override
								public void run() {
									if (!disposed && listener != null) {
										listener.onQueryInventoryFinished(inventory);
									}
									for (Product each : inventory.getProductsWaitingToConsume()) {
										consume(each);
									}
								}
							});
						} else {
							LOGGER.warn("Client disposed. Not queried inventory");
						}
					} catch (final Exception e) {
						if (!disposed && listener != null) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									listener.onQueryInventoryFailed(e instanceof ErrorCodeException ? (ErrorCodeException)e : InAppBillingErrorCode.UNEXPECTED_ERROR.newErrorCodeException(e));
								}
							});
						}
					} finally {
						flagEndAsync();
					}
				}
			});
		}
	}

	@WorkerThread
	private void queryPurchases(Inventory inventory, ItemType itemType) throws ErrorCodeException {

		LOGGER.debug("Querying owned items, item type: " + itemType);
		String continueToken = null;

		try {
			do {
				LOGGER.debug("Calling getPurchases with continuation token: " + continueToken);
				Bundle ownedItems = service.getPurchases(IN_APP_BILLING_API_VERSION, context.getPackageName(),
					itemType.getType(), continueToken);

				InAppBillingErrorCode inAppBillingErrorCode = getResponseCode(ownedItems);
				if (inAppBillingErrorCode != null) {
					throw inAppBillingErrorCode.newErrorCodeException("getPurchases() failed querying " + itemType);
				}
				List<String> ownedProductIds = ownedItems.getStringArrayList(RESPONSE_INAPP_ITEM_LIST);
				if (ownedProductIds == null) {
					throw InAppBillingErrorCode.BAD_RESPONSE.newErrorCodeException("Missing purchase item list from getPurchases()");
				}

				List<String> purchaseDataList = ownedItems.getStringArrayList(RESPONSE_INAPP_PURCHASE_DATA_LIST);
				if (purchaseDataList == null) {
					throw InAppBillingErrorCode.MISSING_PURCHASE_DATA.newErrorCodeException("Missing purchase data list from getPurchases()");
				}

				List<String> signatureList = ownedItems.getStringArrayList(RESPONSE_INAPP_SIGNATURE_LIST);
				if (signatureList == null) {
					throw InAppBillingErrorCode.MISSING_DATA_SIGNATURE.newErrorCodeException("Missing data signature list from getPurchases()");
				}

				for (int i = 0; i < purchaseDataList.size(); ++i) {
					String purchaseData = purchaseDataList.get(i);
					String signature = signatureList.get(i);
					String productId = ownedProductIds.get(i);

					Product product = null;
					if (InAppBillingAppModule.get().getInAppBillingContext().isStaticResponsesEnabledEnabled()) {
						product = inventory.getProductByTestProductId(productId);
					} else {
						product = inventory.getProduct(productId);
					}
					if (product != null) {
						try {
							LOGGER.debug("Setting purchase to product: " + productId + ". " + purchaseData);
							product.setPurchase(signatureBase64, purchaseData, signature, InAppBillingAppModule.get().getDeveloperPayloadVerificationStrategy());
						} catch (ErrorCodeException e) {
							AbstractApplication.get().getExceptionHandler().logHandledException(e);
						}
					} else {
						AbstractApplication.get().getExceptionHandler().logWarningException(
							"The purchased product [" + productId + "] is not supported by the app, so it is ignored");
					}
				}

				// the service returns only up to 700 products that are owned by the user when getPurchase is first called.
				continueToken = ownedItems.getString(INAPP_CONTINUATION_TOKEN);
				LOGGER.debug("Continuation token: " + continueToken);
			} while (!TextUtils.isEmpty(continueToken));

		} catch (RemoteException e) {
			throw InAppBillingErrorCode.REMOTE_EXCEPTION.newErrorCodeException(e);
		} catch (JSONException e) {
			throw InAppBillingErrorCode.BAD_PURCHASE_DATA.newErrorCodeException(e);
		}
	}

	@WorkerThread
	private void queryProductsDetails(Inventory inventory, ItemType itemType, List<ProductType> productTypes)
		throws ErrorCodeException {

		InAppBillingContext inAppBillingContext = InAppBillingAppModule.get().getInAppBillingContext();

		LOGGER.debug("Querying products details.");
		ArrayList<String> productsIdsToQuery = Lists.newArrayList();
		for (ProductType each : productTypes) {
			String productId = inAppBillingContext.isStaticResponsesEnabledEnabled() ? each.getTestProductId() : each.getProductId();
			if (!productsIdsToQuery.contains(productId)) {
				productsIdsToQuery.add(productId);
			}
		}

		try {
			// TODO Query the details on blocks of no more than 20 elements
			if (!productsIdsToQuery.isEmpty()) {
				Bundle bundle = new Bundle();
				bundle.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, productsIdsToQuery);
				Bundle productsDetailsBundle = service.getSkuDetails(IN_APP_BILLING_API_VERSION,
					context.getPackageName(), itemType.getType(), bundle);

				List<String> skuDetailsList = productsDetailsBundle.getStringArrayList(RESPONSE_GET_SKU_DETAILS_LIST);
				if (skuDetailsList != null) {
					if (!skuDetailsList.isEmpty()) {
						Map<String, SkuDetails> map = Maps.newHashMap();
						for (String each : skuDetailsList) {
							SkuDetails skuDetails = new SkuDetails(each);
							map.put(skuDetails.getProductId(), skuDetails);
						}

						for (ProductType each : productTypes) {
							SkuDetails skuDetails = map.get(inAppBillingContext.isStaticResponsesEnabledEnabled() ? each.getTestProductId() : each.getProductId());
							if (skuDetails != null) {
								String title = each.getTitleId() != null ? context.getString(each.getTitleId()) : skuDetails.getTitle();
								String description = each.getDescriptionId() != null ? context.getString(each.getDescriptionId()) : skuDetails.getDescription();
								Product product = new Product(each, skuDetails.getFormattedPrice(),
									skuDetails.getPrice(), skuDetails.getCurrencyCode(), title, description);
								LOGGER.debug("Adding to inventory: " + product);
								inventory.addProduct(product);
							}
						}
					} else {
						LOGGER.debug("No products details returned.");
					}
				} else {
					InAppBillingErrorCode inAppBillingErrorCode = getResponseCode(productsDetailsBundle);
					if (inAppBillingErrorCode != null) {
						throw inAppBillingErrorCode.newErrorCodeException("Failed querying " + itemType);
					} else {
						throw InAppBillingErrorCode.BAD_RESPONSE.newErrorCodeException("getSkuDetails() returned a bundle with neither an error nor a detail list.");
					}
				}
			}
		} catch (RemoteException e) {
			throw InAppBillingErrorCode.REMOTE_EXCEPTION.newErrorCodeException(e);
		} catch (JSONException e) {
			throw InAppBillingErrorCode.BAD_RESPONSE.newErrorCodeException(e);
		}
	}

	@MainThread
	public void launchInAppPurchaseFlow(Activity activity, Product product) {
		launchPurchaseFlow(activity, product, ItemType.MANAGED, null, PURCHASE_REQUEST_CODE);
	}

	@MainThread
	public void launchSubscriptionPurchaseFlow(Activity activity, Product product, List<String> oldProductIds) {
		launchPurchaseFlow(activity, product, ItemType.SUBSCRIPTION, oldProductIds, PURCHASE_REQUEST_CODE);
	}

	/**
	 * Initiate the UI flow for an in-app purchase. Call this method to initiate an in-app purchase, which will involve
	 * bringing up the Google Play screen. The calling activity will be paused while the user interacts with Google
	 * Play, and the result will be delivered via the activity's {@link android.app.Activity#onActivityResult} method,
	 * at which point you must call this object's {@link #handleActivityResult} method to continue the purchase flow.
	 * This method MUST be called from the UI thread of the Activity.
	 *
	 * @param activity The calling activity.
	 * @param product The product to purchase.
	 * @param itemType indicates if it's a product or a subscription (ITEM_TYPE_INAPP or ITEM_TYPE_SUBS)
	 * @param oldProductIds A list of SKUs which the new SKU is replacing or null if there are none
	 * @param requestCode A request code (to differentiate from other responses -- as in
	 * 	{@link android.app.Activity#startActivityForResult}).
	 */
	private void launchPurchaseFlow(Activity activity, Product product, ItemType itemType, List<String> oldProductIds, int requestCode) {
		if (flagStartAsync("launchPurchaseFlow")) {

			if (itemType.equals(ItemType.SUBSCRIPTION) && !subscriptionsSupported) {
				flagEndAsync();
				if (listener != null) {
					listener.onPurchaseFailed(InAppBillingErrorCode.SUBSCRIPTIONS_NOT_AVAILABLE.newErrorCodeException());
				}
				return;
			}

			try {
				LOGGER.debug("Constructing buy intent for product id " + product.getId() + ", item type: " + itemType);
				String productIdToBuy = InAppBillingAppModule.get().getInAppBillingContext().isStaticResponsesEnabledEnabled() ?
					product.getProductType().getTestProductId() : product.getId();
				Bundle buyIntentBundle;
				if (oldProductIds == null || oldProductIds.isEmpty()) {
					// Purchasing a new item or subscription re-signup
					buyIntentBundle = service.getBuyIntent(IN_APP_BILLING_API_VERSION, context.getPackageName(),
						productIdToBuy, itemType.getType(), product.getDeveloperPayload());
				} else {
					// Subscription upgrade/downgrade
					buyIntentBundle = service.getBuyIntentToReplaceSkus(IN_APP_BILLING_SUBSCRIPTION_UPDATE_API_VERSION, context.getPackageName(),
						oldProductIds, product.getId(), itemType.getType(), product.getDeveloperPayload());
				}
				InAppBillingErrorCode inAppBillingErrorCode = getResponseCode(buyIntentBundle);
				if (inAppBillingErrorCode == null) {
					PendingIntent pendingIntent = buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT);
					LOGGER.debug("Launching buy intent for product id " + product.getId() + ". Request code: " + requestCode);
					this.requestCode = requestCode;
					this.productId = product.getId();
					activity.startIntentSenderForResult(pendingIntent.getIntentSender(), requestCode, new Intent(), 0, 0, 0);
				} else {
					flagEndAsync();
					if (listener != null) {
						listener.onPurchaseFailed(inAppBillingErrorCode.newErrorCodeException());
					}
				}
			} catch (SendIntentException e) {
				flagEndAsync();

				if (listener != null) {
					listener.onPurchaseFailed(InAppBillingErrorCode.SEND_INTENT_FAILED.newErrorCodeException(e));
				}
			} catch (RemoteException e) {
				flagEndAsync();

				if (listener != null) {
					listener.onPurchaseFailed(InAppBillingErrorCode.REMOTE_EXCEPTION.newErrorCodeException(e));
				}
			} catch (Exception e) {
				flagEndAsync();

				if (listener != null) {
					listener.onPurchaseFailed(InAppBillingErrorCode.UNEXPECTED_ERROR.newErrorCodeException(e));
				}
			}
		}
	}

	/**
	 * Handles an activity result that's part of the purchase flow in in-app billing. If you are calling
	 * {@link #launchPurchaseFlow}, then you must call this method from your Activity's onActivityResult method. This
	 * method MUST be called from the UI thread of the Activity.
	 *
	 * @param requestCode The requestCode as you received it.
	 * @param resultCode The resultCode as you received it.
	 * @param data The data (Intent) as you received it.
	 * @return Returns true if the result was related to a purchase flow and was handled; false if the result was not
	 * 	related to a purchase, in which case you should handle it normally.
	 */
	@MainThread
	public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
		if (this.requestCode != requestCode) {
			return false;
		}

		// end of async purchase operation that started on launchPurchaseFlow
		flagEndAsync();

		if (data == null) {
			if (listener != null) {
				listener.onPurchaseFailed(InAppBillingErrorCode.BAD_RESPONSE.newErrorCodeException("Null data on activity result."));
			}
			return true;
		}

		InAppBillingErrorCode inAppBillingErrorCode = getResponseCode(data.getExtras());
		if ((resultCode == Activity.RESULT_OK) && (inAppBillingErrorCode == null)) {

			String purchaseJson = data.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
			if (purchaseJson == null) {
				if (listener != null) {
					listener.onPurchaseFailed(InAppBillingErrorCode.MISSING_PURCHASE_DATA.newErrorCodeException("PurchaseData is null. Extras: "
						+ data.getExtras().toString()));
				}
				return true;
			}

			String signature = data.getStringExtra(RESPONSE_INAPP_SIGNATURE);
			if (signature == null) {
				if (listener != null) {
					listener.onPurchaseFailed(InAppBillingErrorCode.MISSING_DATA_SIGNATURE.newErrorCodeException("DataSignature is null. Extras: "
						+ data.getExtras().toString()));
				}
				return true;
			}

			LOGGER.debug("Successful result code from purchase activity.");
			LOGGER.debug("Purchase JSON: " + purchaseJson);
			LOGGER.debug("Data signature: " + signature);
			LOGGER.debug("Extras: " + data.getExtras());

			try {

				Product product = inventory.getProduct(productId);
				try {
					product.setPurchase(signatureBase64, purchaseJson, signature, InAppBillingAppModule.get().getDeveloperPayloadVerificationStrategy());
					LOGGER.debug("Purchase signature successfully verified.");
					InAppBillingAppModule.get().getInAppBillingContext().addPurchasedProductType(product.getProductType());
					InAppBillingAppModule.get().getModuleAnalyticsSender().trackInAppBillingPurchase(product);
					if (listener != null) {
						listener.onPurchaseFinished(product);
					}
					if (product.isWaitingToConsume()) {
						consume(product);
					} else if (listener != null) {
						listener.onProvideProduct(product);
					}
				} catch (ErrorCodeException e) {
					if (listener != null) {
						listener.onPurchaseFailed(e);
					}
				}
			} catch (JSONException e) {
				if (listener != null) {
					listener.onPurchaseFailed(InAppBillingErrorCode.BAD_PURCHASE_DATA.newErrorCodeException(e));
				}
			}

		} else if (resultCode == Activity.RESULT_OK) {
			if (listener != null) {
				listener.onPurchaseFailed(inAppBillingErrorCode.newErrorCodeException("Result code was OK but in-app billing response was not OK. Product id: " + productId));
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			if (listener != null) {
				if (inAppBillingErrorCode != null) {
					listener.onPurchaseFailed(inAppBillingErrorCode.newErrorCodeException("Purchase canceled. Product id: " + productId));
				} else {
					listener.onPurchaseFailed(InAppBillingErrorCode.UNKNOWN_PURCHASE_RESPONSE.newErrorCodeException("Purchase failed. Result code: " + resultCode + ". Product id: " + productId));
				}
			}
		} else {
			if (listener != null) {
				if (inAppBillingErrorCode != null) {
					listener.onPurchaseFailed(inAppBillingErrorCode.newErrorCodeException("Purchase Failed. Result code: " + resultCode + ". Product id: " + productId));
				} else {
					listener.onPurchaseFailed(InAppBillingErrorCode.UNKNOWN_PURCHASE_RESPONSE.newErrorCodeException("Purchase failed. Result code: " + resultCode + ". Product id: " + productId));
				}
			}
		}
		return true;
	}

	/**
	 * Consumes a given in-app product. Consuming can only be done on an item that's owned, and as a result of
	 * consumption, the user will no longer own it.
	 *
	 * @param product The {@link Product} that represents the item to consume.
	 */
	@MainThread
	public void consume(final Product product) {
		final Handler handler = new Handler();
		if (flagStartAsync("consume")) {
			ExecutorUtils.execute(new Runnable() {

				@Override
				public void run() {
					try {
						if (!disposed) {
							if (product.getProductType().getItemType().equals(ItemType.MANAGED)) {
								try {
									String token = product.getPurchase().getToken();
									String productId = product.getId();
									if (StringUtils.isBlank(token)) {
										throw InAppBillingErrorCode.MISSING_TOKEN.newErrorCodeException("Can't consume " + productId + ". No token.");
									}

									LOGGER.debug("Consuming productId: " + productId + ", token: " + token);
									int response = service.consumePurchase(IN_APP_BILLING_API_VERSION, context.getPackageName(), token);
									InAppBillingErrorCode inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(response);
									if (inAppBillingErrorCode == null) {
										product.consume();
										LOGGER.debug("Successfully consumed productId: " + productId);
									} else {
										throw inAppBillingErrorCode.newErrorCodeException("Error consuming consuming productId " + productId);
									}

								} catch (RemoteException e) {
									throw InAppBillingErrorCode.REMOTE_EXCEPTION.newErrorCodeException();
								}

								handler.post(new Runnable() {
									@Override
									public void run() {
										if (!disposed && listener != null) {
											listener.onConsumeFinished(product);
											listener.onProvideProduct(product);
										}
									}
								});

							} else {
								throw InAppBillingErrorCode.INVALID_CONSUMPTION.newErrorCodeException("Items of type '"
									+ product.getProductType().getItemType() + "' can't be consumed.");
							}
						} else {
							LOGGER.warn("Client disposed. Not consuming productId: " + productId);
						}
					} catch (final Exception e) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								if (!disposed && listener != null) {
									listener.onQueryInventoryFailed(e instanceof ErrorCodeException ? (ErrorCodeException)e : InAppBillingErrorCode.UNEXPECTED_ERROR.newErrorCodeException(e));
								}
							}
						});
					} finally {
						flagEndAsync();
					}
				}
			});
		}
	}

	/**
	 * Disposes of object, releasing resources. If there is an in-progress async operation, this
	 * method will queue the dispose to occur after the operation has finished.
	 */
	@MainThread
	public void disposeWhenFinished() {
		synchronized (asyncInProgressLock) {
			if (asyncInProgress) {
				LOGGER.debug("Will dispose after async operation finishes.");
				disposeAfterAsync = true;
			} else {
				dispose();
			}
		}
	}

	/**
	 * Dispose of object, releasing resources. It's very important to call this method when you are done with this
	 * object. It will release any resources used by it such as service connections. Naturally, once the object is
	 * disposed of, it can't be used again.
	 */
	private void dispose() {
		LOGGER.debug("Disposing.");
		if (serviceConnection != null) {
			LOGGER.debug("Unbinding from service.");
			if (context != null) {
				try {
					context.unbindService(serviceConnection);
				} catch (RuntimeException e) {
					LOGGER.warn("Error on unbinding", e);
				}
			}
		}
		disposed = true;
		context = null;
		serviceConnection = null;
		service = null;
		listener = null;
	}

	private Boolean flagStartAsync(String operation) {
		synchronized (asyncInProgressLock) {
			if (asyncInProgress) {
				AbstractApplication.get().getExceptionHandler().logWarningException(
					"Can't start async operation (" + operation + ") because another async operation (" + asyncOperation
						+ ") is in progress.");
				return false;
			}
			asyncOperation = operation;
			asyncInProgress = true;
			LOGGER.debug("Starting async operation: " + operation);
			return true;
		}
	}

	private void flagEndAsync() {
		synchronized (asyncInProgressLock) {
			LOGGER.debug("Ending async operation: " + asyncOperation);
			asyncOperation = null;
			asyncInProgress = false;
			if (disposeAfterAsync) {
				dispose();
			}
		}
	}

	// Workaround to bug where sometimes response codes come as Long instead of Integer
	private InAppBillingErrorCode getResponseCode(Bundle bundle) {
		Object responseCode = bundle.get(RESPONSE_CODE);
		if (responseCode == null) {
			LOGGER.debug("Bundle/Intent with null response code, assuming OK (known issue)");
			return null;
		} else if (responseCode instanceof Integer) {
			return InAppBillingErrorCode.findByErrorResponseCode(((Integer)responseCode));
		} else if (responseCode instanceof Long) {
			return InAppBillingErrorCode.findByErrorResponseCode((int)((Long)responseCode).longValue());
		} else {
			throw InAppBillingErrorCode.UNEXPECTED_ERROR.newErrorCodeException("Unexpected type for bundle response code: "
				+ responseCode.getClass().getName());
		}
	}

	/**
	 * @return whether subscriptions are supported.
	 */
	public boolean isSubscriptionsSupported() {
		return subscriptionsSupported;
	}
}
