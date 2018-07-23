package com.jdroid.android.google.inappbilling.client;

import com.jdroid.java.exception.ErrorCodeException;
import com.jdroid.java.utils.Hasher;

import org.json.JSONException;

import java.util.Locale;

public class Product {
	
	private ProductType productType;
	private String title;
	private String description;
	private Purchase purchase;
	private Boolean availableToPurchase;
	
	private String formattedPrice;
	private Double price;
	private String currencyCode;
	
	public Product(ProductType productType) {
		this.productType = productType;
		availableToPurchase = true;
	}
	
	/**
	 * @return the Formatted price of the item, including its currency sign. The price does not include tax.
	 */
	public String getFormattedPrice() {
		return currencyCode + " " + String.format(Locale.getDefault(),"%.2f", price);
	}
	
	/**
	 * @return The amount the user will pay for the in-app product.
	 */
	public Double getPrice() {
		return price;
	}
	
	/**
	 * @return A short description of the in-app product
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return  A longer description of the in-app product
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return the productType
	 */
	public ProductType getProductType() {
		return productType;
	}
	
	public void setPurchase(String signatureBase64, String purchaseJson, String signature,
							DeveloperPayloadVerificationStrategy developerPayloadVerificationStrategy) throws JSONException, ErrorCodeException {
		purchase = new Purchase(purchaseJson, signatureBase64, signature);
		purchase.verify(this, purchaseJson, developerPayloadVerificationStrategy);
		availableToPurchase = false;
	}
	
	public Boolean hasVerifiedPurchase() {
		return (purchase != null) && (purchase.getState() == Purchase.PurchaseState.PURCHASED) && purchase.isVerified();
	}
	
	public Boolean isAvailableToPurchase() {
		return availableToPurchase;
	}
	
	public void consume() {
		purchase = null;
		availableToPurchase = true;
	}
	
	public Boolean isConsumable() {
		return productType.getItemType().equals(ItemType.MANAGED) && productType.isConsumable();
	}
	
	// TODO Is this status persistent? What happens if the app crashed before consuming a product?
	public Boolean isWaitingToConsume() {
		return isConsumable() && !availableToPurchase && hasVerifiedPurchase();
	}
	
	public String getDeveloperPayload() {
		return Hasher.SHA_1.hash(productType.getProductId());
	}
	
	/**
	 * @return the purchase
	 */
	public Purchase getPurchase() {
		return purchase;
	}
	
	/*
	 * @return ISO 4217 currency code for price
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	public String getId() {
		return getProductType().getProductId();
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setFormattedPrice(String formattedPrice) {
		this.formattedPrice = formattedPrice;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	@Override
	public String toString() {
		return "Product [productType=" + productType + ", title=" + title + ", description=" + description
				+ ", purchase=" + purchase + ", availableToPurchase=" + availableToPurchase + ", formattedPrice=" + formattedPrice
				+ ", price=" + price + ", currencyCode=" + currencyCode + "]";
	}
}
