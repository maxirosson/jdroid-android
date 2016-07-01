package com.jdroid.android.google.inappbilling;

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
import android.text.TextUtils;

import com.android.vending.billing.IInAppBillingService;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.inappbilling.Product.ItemType;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.ErrorCode;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.collections.CollectionUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.json.JSONException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides convenience methods for in-app billing. You can create one instance of this class for your application and
 * use it to process in-app billing operations. It provides synchronous (blocking) and asynchronous (non-blocking)
 * methods for many common in-app billing operations, as well as automatic signature verification.
 * 
 * After instantiating, you must perform setup in order to start using the object. To perform setup, call the
 * {@link #startSetup} method and provide a listener; that listener will be notified when setup is complete, after which
 * (and not before) you may call other methods.
 * 
 * After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See
 * {@link #queryInventory}, {@link #queryInventory(List, List)} and related methods.
 * 
 * When you are done with this object, don't forget to call {@link #dispose} to ensure proper cleanup. This object holds
 * a binding to the in-app billing service, which will leak unless you dispose of it correctly. If you created the
 * object on an Activity's onCreate method, then the recommended place to dispose of it is the Activity's onDestroy
 * method.
 * 
 * A note about threading: When using this object from a background thread, you may call the blocking versions of
 * methods; when using from a UI thread, call only the asynchronous versions and handle the results via callbacks. Also,
 * notice that you can only call one asynchronous operation at a time; attempting to start a second asynchronous
 * operation while the first one has not yet completed will result in an exception being thrown.
 * 
 */
public class InAppBillingClient {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(InAppBillingClient.class);
	
	private static final int IN_APP_BILLING_API_VERSION = 3;
	
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
	
	// Are subscriptions supported?
	private boolean subscriptionsSupported = false;
	
	// Is an asynchronous operation in progress?
	// (only one at a time can be in progress)
	private boolean asyncInProgress = false;
	
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
	 * @param ctx Your application or Activity context. Needed to bind to the in-app billing service.
	 */
	public InAppBillingClient(Context ctx) {
		context = ctx.getApplicationContext();
		signatureBase64 = InAppBillingAppModule.get().getInAppBillingContext().getGooglePlayPublicKey();
		LOGGER.debug("InAppBillingClient created.");
	}
	
	/**
	 * Starts the setup process. This will start up the setup process asynchronously. You will be notified through the
	 * listener when the setup process is complete. This method is safe to call from a UI thread.
	 */
	public void startSetup() {
		
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
						LOGGER.debug("Checking in-app billing " + IN_APP_BILLING_API_VERSION
								+ " support for item type " + ItemType.MANAGED);
						int response = service.isBillingSupported(IN_APP_BILLING_API_VERSION, packageName,
							ItemType.MANAGED.getType());
						ErrorCode inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(response);
						if (inAppBillingErrorCode == null) {
							
							LOGGER.debug("In-app billing supported for item type " + ItemType.MANAGED);
							
							LOGGER.debug("Checking in-app billing " + IN_APP_BILLING_API_VERSION
									+ " support for item type " + ItemType.SUBSCRIPTION);
							response = service.isBillingSupported(IN_APP_BILLING_API_VERSION, packageName,
								ItemType.SUBSCRIPTION.getType());
							inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(response);
							if (inAppBillingErrorCode == null) {
								LOGGER.debug("In-app billing supported for item type " + ItemType.SUBSCRIPTION);
								subscriptionsSupported = true;
							} else {
								LOGGER.warn("Subscriptions NOT AVAILABLE. InAppBillingErrorCode: "
										+ inAppBillingErrorCode);
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
	 * listener upon completion. This method is safe to call from a UI thread.
	 * 
	 * @param managedProductTypes the managed {@link ProductType}s supported by the app
	 * @param subscriptionsProductTypes the subscriptions {@link ProductType}s supported by the app
	 */
	public void queryInventory(final List<ProductType> managedProductTypes,
			final List<ProductType> subscriptionsProductTypes) {
		final Handler handler = new Handler();
		if (flagStartAsync("queryInventory")) {
			ExecutorUtils.execute(() -> {
				try {
					ErrorCodeException errorCodeException = null;
					Inventory inventory = null;
					try {
						inventory = queryInventoryInner(managedProductTypes, subscriptionsProductTypes);
						InAppBillingAppModule.get().getInAppBillingContext().setPurchasedProductTypes(inventory);
					} catch (ErrorCodeException e) {
						errorCodeException = e;
					}

					flagEndAsync();

					final ErrorCodeException errorCodeExceptionFinal = errorCodeException;
					final Inventory inventoryFinal = inventory;
					if (!disposed) {
						handler.post(() -> {
							if (listener != null) {
								if (errorCodeExceptionFinal == null) {
									listener.onQueryInventoryFinished(inventoryFinal);
								} else {
									listener.onQueryInventoryFailed(errorCodeExceptionFinal);
								}
							}
						});
					}
				} catch (Exception e) {
					if (listener != null) {
						listener.onQueryInventoryFailed(InAppBillingErrorCode.UNEXPECTED_ERROR.newErrorCodeException(e));
					}
				}
            });
		}
	}
	
	/**
	 * Queries the inventory. This will query all owned items from the server, as well as information on additional skus
	 * This method may block or take long to execute. Do not call from a UI thread.
	 * 
	 * @param managedProductTypes the managed {@link ProductType}s supported by the app
	 * @param subscriptionsProductTypes the subscriptions {@link ProductType}s supported by the app
	 * @return The {@link Inventory}
	 * @throws ErrorCodeException if a problem occurs while refreshing the inventory.
	 */
	private Inventory queryInventoryInner(List<ProductType> managedProductTypes,
			List<ProductType> subscriptionsProductTypes) throws ErrorCodeException {
		inventory = null;
		if (!disposed) {
			inventory = new Inventory();
			queryProductsDetails(inventory, ItemType.MANAGED, managedProductTypes);
			queryPurchases(inventory, ItemType.MANAGED);
			
			// if subscriptions are supported, then also query for subscriptions
			if (subscriptionsSupported) {
				queryProductsDetails(inventory, ItemType.SUBSCRIPTION, subscriptionsProductTypes);
				queryPurchases(inventory, ItemType.SUBSCRIPTION);
			}
			LOGGER.debug("Query inventory was successful.");
		} else {
			LOGGER.warn("Client disposed. Not queried inventary");
		}
		
		return inventory;
	}
	
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
					
					Product product = inventory.getProduct(productId);
					if (product != null) {
						try {
							LOGGER.debug("Setting purchase to product: " + productId + ". " + purchaseData);
							product.setPurchase(signatureBase64, purchaseData, signature);
						} catch (ErrorCodeException e) {
							AbstractApplication.get().getExceptionHandler().logHandledException(e);
						}
					} else {
						AbstractApplication.get().getExceptionHandler().logWarningException(
							"The purchased product [" + productId
									+ "] is not supported any more by the app, so it is ignored");
					}
				}
				
				continueToken = ownedItems.getString(INAPP_CONTINUATION_TOKEN);
				LOGGER.debug("Continuation token: " + continueToken);
			} while (!TextUtils.isEmpty(continueToken));
			
		} catch (RemoteException e) {
			throw InAppBillingErrorCode.REMOTE_EXCEPTION.newErrorCodeException(e);
		} catch (JSONException e) {
			throw InAppBillingErrorCode.BAD_PURCHASE_DATA.newErrorCodeException(e);
		}
	}
	
	private void queryProductsDetails(Inventory inventory, ItemType itemType, List<ProductType> productTypes)
			throws ErrorCodeException {
		
		LOGGER.debug("Querying products details.");
		ArrayList<String> productsIdsToQuery = Lists.newArrayList();
		for (ProductType each : productTypes) {
			if (!productsIdsToQuery.contains(each.getProductId())) {
				productsIdsToQuery.add(each.getProductId());
			}
		}
		
		try {
			if (!productsIdsToQuery.isEmpty()) {
				Bundle bundle = new Bundle();
				bundle.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, productsIdsToQuery);
				Bundle productsDetailsBundle = service.getSkuDetails(IN_APP_BILLING_API_VERSION,
					context.getPackageName(), itemType.getType(), bundle);
				
				List<String> skuDetailsList = productsDetailsBundle.getStringArrayList(RESPONSE_GET_SKU_DETAILS_LIST);
				Map<String, SkuDetails> map = Maps.newHashMap();
				if (skuDetailsList != null) {
					
					for (String each : skuDetailsList) {
						SkuDetails skuDetails = new SkuDetails(each);
						map.put(skuDetails.getSku(), skuDetails);
					}
					
					for (ProductType each : productTypes) {
						SkuDetails skuDetails = map.get(each.getProductId());
						if (skuDetails != null) {
							InAppBillingContext inAppBillingContext = InAppBillingAppModule.get().getInAppBillingContext();
							ProductType productType = inAppBillingContext.isInAppBillingMockEnabled() ? inAppBillingContext.getTestProductType()
									: each;
							
							String title = each.getTitleId() != null ? context.getString(each.getTitleId()) : null;
							String description = each.getDescriptionId() != null ? context.getString(each.getDescriptionId())
									: null;
							Product product = new Product(productType, skuDetails.getFormattedPrice(),
									skuDetails.getPrice(), skuDetails.getCurrencyCode(), title, description);
							LOGGER.debug("Adding to inventory: " + product);
							inventory.addProduct(product);
						}
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
	
	public void launchInAppPurchaseFlow(Activity activity, String productId, String devloperPayload) {
		launchPurchaseFlow(activity, productId, ItemType.MANAGED, PURCHASE_REQUEST_CODE, devloperPayload);
	}
	
	public void launchSubscriptionPurchaseFlow(Activity activity, String productId, int requestCode,
			String devloperPayload) {
		launchPurchaseFlow(activity, productId, ItemType.SUBSCRIPTION, PURCHASE_REQUEST_CODE, devloperPayload);
	}
	
	/**
	 * Initiate the UI flow for an in-app purchase. Call this method to initiate an in-app purchase, which will involve
	 * bringing up the Google Play screen. The calling activity will be paused while the user interacts with Google
	 * Play, and the result will be delivered via the activity's {@link android.app.Activity#onActivityResult} method,
	 * at which point you must call this object's {@link #handleActivityResult} method to continue the purchase flow.
	 * This method MUST be called from the UI thread of the Activity.
	 * 
	 * @param activity The calling activity.
	 * @param productId The product id of the item to purchase.
	 * @param itemType indicates if it's a product or a subscription (ITEM_TYPE_INAPP or ITEM_TYPE_SUBS)
	 * @param requestCode A request code (to differentiate from other responses -- as in
	 *            {@link android.app.Activity#startActivityForResult}).
	 * @param devloperPayload The developer payload, which will be returned with the purchase data when the purchase
	 *            completes. This extra data will be permanently bound to that purchase and will always be returned when
	 *            the purchase is queried.
	 */
	private void launchPurchaseFlow(Activity activity, String productId, ItemType itemType, int requestCode,
			String devloperPayload) {
		if (flagStartAsync("launchPurchaseFlow")) {
			
			if (itemType.equals(ItemType.SUBSCRIPTION) && !subscriptionsSupported) {
				flagEndAsync();
				if (listener != null) {
					listener.onPurchaseFailed(InAppBillingErrorCode.SUBSCRIPTIONS_NOT_AVAILABLE.newErrorCodeException());
				}
				return;
			}
			
			try {
				LOGGER.debug("Constructing buy intent for product id " + productId + ", item type: " + itemType);
				Bundle buyIntentBundle = service.getBuyIntent(IN_APP_BILLING_API_VERSION, context.getPackageName(),
					productId, itemType.getType(), devloperPayload);
				InAppBillingErrorCode inAppBillingErrorCode = getResponseCode(buyIntentBundle);
				if (inAppBillingErrorCode == null) {
					PendingIntent pendingIntent = buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT);
					LOGGER.debug("Launching buy intent for product id " + productId + ". Request code: " + requestCode);
					this.requestCode = requestCode;
					this.productId = productId;
					activity.startIntentSenderForResult(pendingIntent.getIntentSender(), requestCode, new Intent(), 0,
						0, 0);
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
	 *         related to a purchase, in which case you should handle it normally.
	 */
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
			
			String purchaseData = data.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
			if (purchaseData == null) {
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
			
			LOGGER.debug("Successful resultcode from purchase activity.");
			LOGGER.debug("Purchase data: " + purchaseData);
			LOGGER.debug("Data signature: " + signature);
			LOGGER.debug("Extras: " + data.getExtras());
			
			try {
				
				Product product = inventory.getProduct(productId);
				try {
					product.setPurchase(signatureBase64, purchaseData, signature);
					LOGGER.debug("Purchase signature successfully verified.");
					InAppBillingAppModule.get().getInAppBillingContext().addPurchasedProductType(product.getProductType());
					InAppBillingAppModule.get().getAnalyticsSender().trackInAppBillingPurchase(product);
					if (listener != null) {
						listener.onPurchaseFinished(product);
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
				listener.onPurchaseFailed(inAppBillingErrorCode.newErrorCodeException("Result code was OK but in-app billing response was not OK"));
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			if (listener != null) {
				if (inAppBillingErrorCode != null) {
					listener.onPurchaseFailed(inAppBillingErrorCode.newErrorCodeException("Purchase Failed. Result code: "
							+ Integer.toString(resultCode)));
				} else {
					listener.onPurchaseFailed(InAppBillingErrorCode.USER_CANCELED.newErrorCodeException("Purchase canceled. Result code: "
							+ Integer.toString(resultCode)));
				}
			}
		} else {
			if (listener != null) {
				if (inAppBillingErrorCode != null) {
					listener.onPurchaseFailed(inAppBillingErrorCode.newErrorCodeException("Purchase Failed. Result code: "
							+ Integer.toString(resultCode)));
				} else {
					listener.onPurchaseFailed(InAppBillingErrorCode.UNKNOWN_PURCHASE_RESPONSE.newErrorCodeException("Purchase failed. Result code: "
							+ Integer.toString(resultCode)));
				}
			}
		}
		return true;
	}
	
	/**
	 * Asynchronous wrapper to item consumption. Works like {@link #consume}, but performs the consumption in the
	 * background and notifies completion through the provided listener. This method is safe to call from a UI thread.
	 * 
	 * @param product The {@link Product} to be consumed.
	 */
	public void consume(final Product product) {
		final Handler handler = new Handler();
		if (flagStartAsync("consume")) {
			ExecutorUtils.execute(() -> {
                try {
                    ErrorCodeException errorCodeException = null;
                    try {
                        consumeInner(product);
                    } catch (ErrorCodeException e) {
                        errorCodeException = e;
                    }

                    flagEndAsync();

                    final ErrorCodeException errorCodeExceptionFinal = errorCodeException;
                    if (!disposed) {
                        handler.post(() -> {
                            if (listener != null) {
                                if (errorCodeExceptionFinal == null) {
                                    listener.onConsumeFinished(product);
                                } else {
                                    listener.onConsumeFailed(errorCodeExceptionFinal);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onQueryInventoryFailed(InAppBillingErrorCode.UNEXPECTED_ERROR.newErrorCodeException(e));
                    }
                }
            });
		}
	}
	
	/**
	 * Consumes a given in-app product. Consuming can only be done on an item that's owned, and as a result of
	 * consumption, the user will no longer own it. This method may block or take long to return. Do not call from the
	 * UI thread.
	 * 
	 * @param product The {@link Product} that represents the item to consume.
	 */
	private void consumeInner(Product product) throws ErrorCodeException {
		
		if (product.getProductType().getItemType().equals(ItemType.MANAGED)) {
			try {
				String token = product.getPurchase().getToken();
				String productId = product.getId();
				if ((token == null) || token.equals("")) {
					throw InAppBillingErrorCode.MISSING_TOKEN.newErrorCodeException("Can't consume " + productId
							+ ". No token.");
				}
				
				if (!disposed) {
					LOGGER.debug("Consuming productId: " + productId + ", token: " + token);
					int response = service.consumePurchase(IN_APP_BILLING_API_VERSION, context.getPackageName(), token);
					InAppBillingErrorCode inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(response);
					if (inAppBillingErrorCode == null) {
						product.consume();
						LOGGER.debug("Successfully consumed productId: " + productId);
					} else {
						throw inAppBillingErrorCode.newErrorCodeException("Error consuming consuming productId "
								+ productId);
					}
				} else {
					LOGGER.warn("Client disposed. Not consuming productId: " + productId + ", token: " + token);
				}
			} catch (RemoteException e) {
				throw InAppBillingErrorCode.REMOTE_EXCEPTION.newErrorCodeException();
			}
		} else {
			throw InAppBillingErrorCode.INVALID_CONSUMPTION.newErrorCodeException("Items of type '"
					+ product.getProductType().getItemType() + "' can't be consumed.");
		}
	}
	
	/**
	 * Dispose of object, releasing resources. It's very important to call this method when you are done with this
	 * object. It will release any resources used by it such as service connections. Naturally, once the object is
	 * disposed of, it can't be used again.
	 */
	public void dispose() {
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
		if (asyncInProgress) {
			AbstractApplication.get().getExceptionHandler().logWarningException(
				"Can't start async operation (consumeAsyncInternal) because another (" + asyncOperation
						+ ") is in progress.");
			return false;
		}
		asyncOperation = operation;
		asyncInProgress = true;
		LOGGER.debug("Starting async operation: " + operation);
		return true;
	}
	
	private void flagEndAsync() {
		LOGGER.debug("Ending async operation: " + asyncOperation);
		asyncOperation = null;
		asyncInProgress = false;
	}
	
	// Workaround to bug where sometimes response codes come as Long instead of Integer
	private InAppBillingErrorCode getResponseCode(Bundle bundle) {
		Object responseCode = bundle.get(RESPONSE_CODE);
		if (responseCode == null) {
			LOGGER.debug("Bundle/Intent with null response code, assuming OK (known issue)");
			return null;
		} else if (responseCode instanceof Integer) {
			return InAppBillingErrorCode.findByErrorResponseCode(((Integer)responseCode).intValue());
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
	public boolean subscriptionsSupported() {
		return subscriptionsSupported;
	}
	
	public void setInAppBillingClientListener(InAppBillingClientListener listener) {
		this.listener = listener;
	}
}
