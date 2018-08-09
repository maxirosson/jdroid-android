package com.jdroid.android.google.inappbilling.client;


import android.support.annotation.Nullable;

public interface ProductType {
	
	/*
	 * @return A unique, human readable ID for your product. Product IDs are also called SKUs
	 */
	public String getProductId();
	
	/*
	 * @return The product ID for the product used when the static responses is enabled
	 */
	public String getTestProductId();
	
	/*
	 * Non-consumable Items. Once purchased, these items will be permanently associated to the user's Google account.
	 * A one-time product that can be used indefinitely is called a non-consumable.
	 * An example of a non-consumable in-app product is a premium upgrade or a level pack.
	 *
	 * Consumable items. Items that can be made available for purchase multiple times.
	 * A one-time product with non-infinite use is called a consumable.
	 */
	public Boolean isConsumable();
	
	public ItemType getItemType();
	
	public Integer getTitleId();
	
	public Integer getDescriptionId();
	
	@Nullable
	public Integer getIconId();
	
}
