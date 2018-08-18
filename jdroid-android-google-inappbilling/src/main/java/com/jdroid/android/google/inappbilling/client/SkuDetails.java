package com.jdroid.android.google.inappbilling.client;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents an in-app product's listing details.
 */
public class SkuDetails {

	private String productId;
	private String itemType;
	private String title;
	private String description;

	private String formattedPrice;
	private Double price;
	private String currencyCode;

	public SkuDetails(String jsonSkuDetails) throws JSONException {
		JSONObject o = new JSONObject(jsonSkuDetails);
		productId = o.optString("productId");
		itemType = o.optString("type");
		title = o.optString("title");
		description = o.optString("description");

		formattedPrice = o.optString("price");
		price = o.optDouble("price_amount_micros") / 1000000;
		currencyCode = o.optString("price_currency_code");

	}

	public String getProductId() {
		return productId;
	}

	public String getItemType() {
		return itemType;
	}

	public String getFormattedPrice() {
		return formattedPrice;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	@Override
	public String toString() {
		return "SkuDetails [itemType=" + itemType + ", productId=" + productId + ", formattedPrice=" + formattedPrice
			+ ", title=" + title + ", description=" + description + ", price=" + price + ", currencyCode="
			+ currencyCode + "]";
	}

}
