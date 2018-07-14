package com.jdroid.android.google.inappbilling;

import com.jdroid.android.context.AbstractAppContext;
import com.jdroid.android.google.inappbilling.client.Inventory;
import com.jdroid.android.google.inappbilling.client.Product;
import com.jdroid.android.google.inappbilling.client.ProductType;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class InAppBillingContext extends AbstractAppContext {
	
	public static final String SHARED_PREFERENCES_NAME = "InAppBilling";
	
	public static final String STATIC_RESPONSES_ENABLED = "staticResponsesEnabled";
	private static final String PURCHASED_PRODUCT_IDS = "purchasedProductIds";
	
	private String googlePlayPublicKey;
	private List<ProductType> purchasedProductTypes;

	public InAppBillingContext() {
		// TODO Instead of just storing the entire literal string here embedded in the program, construct the key at
		// runtime from pieces or use bit manipulation (for example, XOR with some other string) to hide the actual key.
		// The key itself is not secret information, but we don't want to make it easy for an attacker to replace the
		// public key with one of their own and then fake messages from the server.
		googlePlayPublicKey = getBuildConfigValue("GOOGLE_PLAY_PUBLIC_KEY");
		
	}
	
	/**
	 * @return Your application's public key, encoded in base64. This is used for verification of purchase signatures.
	 *         You can find your app's base64-encoded public key in your application's page on Google Play Developer
	 *         Console. Note that this is NOT your "developer public key".
	 */
	public String getGooglePlayPublicKey() {
		return googlePlayPublicKey;
	}
	
	public Boolean isStaticResponsesEnabled() {
		return !AppUtils.isReleaseBuildType() && getSharedPreferencesHelper().loadPreferenceAsBoolean(STATIC_RESPONSES_ENABLED, false);
	}
	
	public synchronized void setPurchasedProductTypes(Inventory inventory) {
		purchasedProductTypes = Lists.newArrayList();
		List<String> productIds = Lists.newArrayList();
		for (Product each : inventory.getSupportedPurchasedProducts()) {
			productIds.add(each.getId());
			purchasedProductTypes.add(each.getProductType());
		}
		getSharedPreferencesHelper().savePreferenceAsync(PURCHASED_PRODUCT_IDS, productIds);
	}
	
	public synchronized void addPurchasedProductType(ProductType productType) {
		if (purchasedProductTypes == null) {
			purchasedProductTypes = Lists.newArrayList();
		}
		purchasedProductTypes.add(productType);
		getSharedPreferencesHelper().appendPreferenceAsync(PURCHASED_PRODUCT_IDS, productType.getProductId());
	}

	public synchronized List<ProductType> getPurchasedProductTypes() {
		if (purchasedProductTypes == null) {
			purchasedProductTypes = Lists.newArrayList();
			List<String> purchasedProductIds = getSharedPreferencesHelper().loadPreferenceAsStringList(PURCHASED_PRODUCT_IDS);
			for (String each : purchasedProductIds) {
				List<ProductType> supportedProductTypes = Lists.newArrayList();
				supportedProductTypes.addAll(getManagedProductTypes());
				supportedProductTypes.addAll(getSubscriptionsProductTypes());
				for (ProductType supportedProductType : supportedProductTypes) {
					if (supportedProductType.getProductId().equals(each)) {
						purchasedProductTypes.add(supportedProductType);
						break;
					}
				}
			}
		}
		return purchasedProductTypes;
	}

	public List<ProductType> getManagedProductTypes() {
		return Lists.newArrayList();
	}

	public List<ProductType> getSubscriptionsProductTypes() {
		return Lists.newArrayList();
	}
	
	public SharedPreferencesHelper getSharedPreferencesHelper() {
		return SharedPreferencesHelper.get(SHARED_PREFERENCES_NAME);
	}
}
