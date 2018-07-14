package com.jdroid.android.google.inappbilling.client;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.inappbilling.InAppBillingAppModule;
import com.jdroid.android.google.inappbilling.InAppBillingContext;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;

import org.json.JSONException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides convenience methods for in-app billing. You can create one instance of this class for your application and
 * use it to process in-app billing operations. It provides asynchronous (non-blocking)
 * methods for many common in-app billing operations, as well as automatic signature verification.
 * 
 * After instantiating, you must perform setup in order to start using the object. To perform setup, call the
 * {@link #startSetup} method and provide a listener; that listener will be notified when setup is complete, after which
 * (and not before) you may call other methods.
 * 
 * After setup is complete, you will typically want to request an inventory of owned items and subscriptions. See
 * {@link #queryInventory} and related methods.
 * 
 * A note about threading: When using this object from a background thread, you may call the blocking versions of
 * methods; when using from a UI thread, call only the asynchronous versions and handle the results via callbacks. Also,
 * notice that you can only call one asynchronous operation at a time; attempting to start a second asynchronous
 * operation while the first one has not yet completed will result in an exception being thrown.
 * 
 */
public class InAppBillingClient implements PurchasesUpdatedListener {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(InAppBillingClient.class);
	
	private BillingClient billingClient;
	
	// True if billing service is connected now.
	private boolean isServiceConnected;
	
	private Integer billingClientResponseCode;
	
	private Set<String> tokensToBeConsumed;
	
	///////
	
	// Are subscriptions supported?
	private Boolean subscriptionsSupported;
	
	// Context we were passed during initialization
	private Context context;
	
	// Public key for verifying signature, in base64 encoding
	private String signatureBase64;
	
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
			
			billingClient = BillingClient.newBuilder(context).setListener(this).build();
			startServiceConnection(new Runnable() {
				@Override
				public void run() {
					listener.onSetupFinished();
				}
			});
			
		} catch (Exception e) {
			if (listener != null) {
				listener.onSetupFailed(InAppBillingErrorCode.UNEXPECTED_ERROR.newErrorCodeException(e));
			}
		}
	}
	
	public void startServiceConnection(final Runnable executeOnSuccess) {
		billingClient.startConnection(new BillingClientStateListener() {
			@Override
			public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
				LOGGER.debug("Setup finished. Response code: " + billingResponseCode);
				
				if (billingResponseCode == BillingClient.BillingResponse.OK) {
					isServiceConnected = true;
					if (executeOnSuccess != null) {
						executeOnSuccess.run();
					}
				}
				billingClientResponseCode = billingResponseCode;
			}
			
			@Override
			public void onBillingServiceDisconnected() {
				isServiceConnected = false;
			}
		});
	}
	
	/**
	 * Checks if subscriptions are supported for current client
	 * <p>Note: This method does not automatically retry for RESULT_SERVICE_DISCONNECTED.
	 * It is only used in unit tests and after queryPurchases execution, which already has
	 * a retry-mechanism implemented.
	 * </p>
	 */
	public boolean areSubscriptionsSupported() {
		int responseCode = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
		if (responseCode != BillingClient.BillingResponse.OK) {
			LOGGER.debug("In-app billing subscriptions supported");
			subscriptionsSupported = true;
			return true;
		} else {
			InAppBillingErrorCode inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(responseCode);
			LOGGER.warn("Subscriptions NOT AVAILABLE. InAppBillingErrorCode: " + inAppBillingErrorCode);
			subscriptionsSupported = false;
			return false;
		}
	}
	
	private void executeServiceRequest(Runnable runnable) {
		if (isServiceConnected) {
			runnable.run();
		} else {
			// If billing service was disconnected, we try to reconnect 1 time.
			// TODO (feel free to introduce your retry policy here).
			startServiceConnection(runnable);
		}
	}
	
	/**
	 * Returns the value Billing client response code or null if the client connection response was not received yet.
	 */
	public int getBillingClientResponseCode() {
		return billingClientResponseCode;
	}
	
	public void onDestroy() {
		
		if (billingClient != null && billingClient.isReady()) {
			billingClient.endConnection();
			billingClient = null;
		}
	}
	
	//////////////////////////////////
	
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
		executeServiceRequest(new Runnable() {
			
			@Override
			public void run() {
				try {
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
							if (listener != null) {
								listener.onQueryInventoryFinished(inventory);
							}
							for (Product each : inventory.getProductsWaitingToConsume()) {
								consume(each);
							}
						}
					});
				} catch (final Exception e) {
					if (listener != null) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								listener.onQueryInventoryFailed(e instanceof ErrorCodeException ? (ErrorCodeException)e : InAppBillingErrorCode.UNEXPECTED_ERROR.newErrorCodeException(e));
							}
						});
					}
				}
			}
		});
	}
	
	private void queryPurchases(Inventory inventory, ItemType itemType) throws ErrorCodeException {
		
		LOGGER.debug("Querying owned items, item type: " + itemType);
		String continueToken = null;
		
		try {
			LOGGER.debug("Calling getPurchases with continuation token: " + continueToken);
			Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
			
			InAppBillingErrorCode inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(purchasesResult.getResponseCode());
			if (inAppBillingErrorCode != null) {
				throw inAppBillingErrorCode.newErrorCodeException("getPurchases() failed querying " + itemType);
			}
			
			for (Purchase purchase : purchasesResult.getPurchasesList()) {
				String purchaseJson = purchase.getOriginalJson();
				String signature = purchase.getSignature();
				String productId = purchase.getSku();
				
				Product product = null;
				if (InAppBillingAppModule.get().getInAppBillingContext().isStaticResponsesEnabled()) {
					product = inventory.getProductByTestProductId(productId);
				} else {
					product = inventory.getProduct(productId);
				}
				if (product != null) {
					try {
						LOGGER.debug("Setting purchase to product: " + productId + ". " + purchaseJson);
						product.setPurchase(signatureBase64, purchaseJson, signature, InAppBillingAppModule.get().getDeveloperPayloadVerificationStrategy());
					} catch (ErrorCodeException e) {
						AbstractApplication.get().getExceptionHandler().logHandledException(e);
					}
				} else {
					AbstractApplication.get().getExceptionHandler().logWarningException(
							"The purchased product [" + productId + "] is not supported by the app, so it is ignored");
				}
			}
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
			String productId = inAppBillingContext.isStaticResponsesEnabled() ? each.getTestProductId() : each.getProductId();
			if (!productsIdsToQuery.contains(productId)) {
				productsIdsToQuery.add(productId);
			}
		}
		
		if (!productsIdsToQuery.isEmpty()) {
			
			Runnable queryRequest = new Runnable() {
				@Override
				public void run() {
					SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
					params.setSkusList(productsIdsToQuery).setType(itemType.getType());
					billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
						@Override
						public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
							if (responseCode == BillingClient.BillingResponse.OK) {
								Map<String, SkuDetails> map = Maps.newHashMap();
								for (SkuDetails skuDetails : skuDetailsList) {
									map.put(skuDetails.getSku(), skuDetails);
								}
								
								for (ProductType each : productTypes) {
									SkuDetails skuDetails = map.get(inAppBillingContext.isStaticResponsesEnabled() ? each.getTestProductId() : each.getProductId());
									if (skuDetails != null) {
										String title = each.getTitleId() != null ? context.getString(each.getTitleId()) : skuDetails.getTitle();
										String description = each.getDescriptionId() != null ? context.getString(each.getDescriptionId()) : skuDetails.getDescription();
										Product product = new Product(each, skuDetails.getPrice(),
												(double)skuDetails.getPriceAmountMicros() / 1000000, skuDetails.getPriceCurrencyCode(), title, description);
										LOGGER.debug("Adding to inventory: " + product);
										inventory.addProduct(product);
									}
								}
							} else {
								InAppBillingErrorCode inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(responseCode);
								throw inAppBillingErrorCode.newErrorCodeException("Failed querying " + itemType);
							}
						}
					});
				}
			};
			executeServiceRequest(queryRequest);
		}
	}
	
	@MainThread
	public void launchInAppPurchaseFlow(Activity activity, Product product) {
		launchPurchaseFlow(activity, product, ItemType.MANAGED, null);
	}
	
	@MainThread
	public void launchSubscriptionPurchaseFlow(Activity activity, Product product, String oldProductId) {
		launchPurchaseFlow(activity, product, ItemType.SUBSCRIPTION, oldProductId);
	}
	
	/**
	 * Initiate the UI flow for an in-app purchase. Call this method to initiate an in-app purchase, which will involve
	 * bringing up the Google Play screen. The calling activity will be paused while the user interacts with Google
	 * Play
	 * This method MUST be called from the UI thread of the Activity.
	 * 
	 * @param activity The calling activity.
	 * @param product The product to purchase.
	 * @param itemType indicates if it's a product or a subscription (ITEM_TYPE_INAPP or ITEM_TYPE_SUBS)
	 * @param oldProductId The SKU which the new SKU is replacing or null if there is none
	 */
	private void launchPurchaseFlow(Activity activity, Product product, ItemType itemType, String oldProductId) {
		if (itemType.equals(ItemType.SUBSCRIPTION) && !subscriptionsSupported) {
			if (listener != null) {
				listener.onPurchaseFailed(InAppBillingErrorCode.SUBSCRIPTIONS_NOT_AVAILABLE.newErrorCodeException());
			}
			return;
		}
		Runnable purchaseFlowRequest = new Runnable() {
			@Override
			public void run() {
				LOGGER.debug("Launching in-app purchase flow for product id " + product.getId() + ", item type: " + itemType);
				String productIdToBuy = InAppBillingAppModule.get().getInAppBillingContext().isStaticResponsesEnabled() ?
						product.getProductType().getTestProductId() : product.getId();
				BillingFlowParams purchaseParams = BillingFlowParams.newBuilder()
						.setSku(productIdToBuy).setType(itemType.getType()).setOldSku(oldProductId).build();
				InAppBillingClient.this.productId = product.getId();
				int responseCode = billingClient.launchBillingFlow(activity, purchaseParams);
				if (responseCode != BillingClient.BillingResponse.OK) {
					InAppBillingErrorCode inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(responseCode);
					if (listener != null) {
						listener.onPurchaseFailed(inAppBillingErrorCode.newErrorCodeException());
					}
				}
				
				
			}
		};
		executeServiceRequest(purchaseFlowRequest);
	}
	
	/**
	 * Handle a callback that purchases were updated from the Billing library
	 */
	@Override
	public void onPurchasesUpdated(int resultCode, List<Purchase> purchases) {
		if (resultCode == BillingClient.BillingResponse.OK) {
			for (Purchase purchase : purchases) {
				Product product = inventory.getProduct(productId);
				try {
					product.setPurchase(signatureBase64, purchase.getOriginalJson(), purchase.getSignature(), InAppBillingAppModule.get().getDeveloperPayloadVerificationStrategy());
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
				} catch (JSONException e) {
					if (listener != null) {
						listener.onPurchaseFailed(InAppBillingErrorCode.BAD_PURCHASE_DATA.newErrorCodeException(e));
					}
				}
			}
		} else if (resultCode == BillingClient.BillingResponse.USER_CANCELED) {
			if (listener != null) {
				LOGGER.warn("User cancelled the purchase flow. Product id: " + productId);
				listener.onPurchaseFailed(InAppBillingErrorCode.USER_CANCELED.newErrorCodeException("User cancelled the purchase flow. Product id: " + productId));
			}
		} else {
			InAppBillingErrorCode inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(resultCode);
			LOGGER.error("Purchase failed. Result code: " + resultCode + ". Product id: " + productId);
			listener.onPurchaseFailed(inAppBillingErrorCode.newErrorCodeException("Purchase failed. Result code: " + resultCode + ". Product id: " + productId));
			
		}
	}
	
	/**
	 * Consumes a given in-app product. Consuming can only be done on an item that's owned, and as a result of
	 * consumption, the user will no longer own it.
	 * 
	 * @param product The {@link Product} that represents the item to consume.
	 */
	@MainThread
	public void consume(final Product product) {
		
		if (product.getProductType().getItemType().equals(ItemType.MANAGED)) {
			String token = product.getPurchase().getToken();
			String productId = product.getId();
			if (StringUtils.isBlank(token)) {
				throw InAppBillingErrorCode.MISSING_TOKEN.newErrorCodeException("Can't consume " + product.getId() + ". No token.");
			}
			
			// If we've already scheduled to consume this token - no action is needed (this could happen
			// if you received the token when querying purchases inside onReceive() and later from
			// onActivityResult()
			if (tokensToBeConsumed == null) {
				tokensToBeConsumed = new HashSet<>();
			} else if (tokensToBeConsumed.contains(token)) {
				LOGGER.debug("Token was already scheduled to be consumed - skipping...");
				return;
			}
			tokensToBeConsumed.add(token);
			
			
			LOGGER.debug("Consuming productId: " + productId + ", token: " + token);
			
			// Generating Consume Response listener
			final ConsumeResponseListener onConsumeListener = new ConsumeResponseListener() {
				@Override
				public void onConsumeResponse(@BillingClient.BillingResponse int responseCode, String purchaseToken) {
					if (responseCode == BillingClient.BillingResponse.OK) {
						LOGGER.debug("Successfully consumed productId: " + productId);
						if (listener != null) {
							listener.onConsumeFinished(product);
							listener.onProvideProduct(product);
						}
					} else {
						InAppBillingErrorCode inAppBillingErrorCode = InAppBillingErrorCode.findByErrorResponseCode(responseCode);
						throw inAppBillingErrorCode.newErrorCodeException("Consume failed.");
					}
				}
			};
			
			// Creating a runnable from the request to use it inside our connection retry policy below
			Runnable consumeRequest = new Runnable() {
				@Override
				public void run() {
					billingClient.consumeAsync(token, onConsumeListener);
				}
			};
			
			executeServiceRequest(consumeRequest);
		} else {
			throw InAppBillingErrorCode.INVALID_CONSUMPTION.newErrorCodeException("Items of type '"
					+ product.getProductType().getItemType() + "' can't be consumed.");
		}
	}
	
	/**
	 * @return whether subscriptions are supported.
	 */
	public boolean isSubscriptionsSupported() {
		return subscriptionsSupported;
	}
}
