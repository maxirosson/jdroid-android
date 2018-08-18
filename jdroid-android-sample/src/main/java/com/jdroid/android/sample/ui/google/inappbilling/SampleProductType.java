package com.jdroid.android.sample.ui.google.inappbilling;


import com.jdroid.android.google.inappbilling.client.ItemType;
import com.jdroid.android.google.inappbilling.client.ProductType;
import com.jdroid.android.google.inappbilling.client.TestProductType;

public enum SampleProductType implements ProductType {

	CONSUMABLE_PURCHASED("sample.consumable.purchased.id", TestProductType.PURCHASED, true, null, null),
	CONSUMABLE_CANCELED("sample.consumable.canceled.id", TestProductType.CANCELED, true, null, null),
	CONSUMABLE_REFUNDED("sample.consumable.refunded.id", TestProductType.REFUNDED, true, null, null),
	CONSUMABLE_UNAVAILABLED("sample.consumable.unavailable.id", TestProductType.UNAVAILABLE, true, null, null);
//	NOT_CONSUMABLE_PURCHASED("sample.not.consumable.purchased.id", TestProductType.PURCHASED, false, null, null),
//	NOT_CONSUMABLE_CANCELED("sample.not.consumable.canceled.id", TestProductType.CANCELED, false, null, null),
//	NOT_CONSUMABLE_REFUNDED("sample.not.consumable.refunded.id", TestProductType.REFUNDED, false, null, null),
//	NOT_CONSUMABLE_UNAVAILABLE("sample.not.consumable.unavailable.id", TestProductType.UNAVAILABLE, false, null, null);

	private String productId;
	private String testProductId;
	private Boolean consumable;
	private Integer titleId;
	private Integer descriptionId;

	private SampleProductType(String productId, String testProductId, Boolean consumable, Integer titleId, Integer descriptionId) {
		this.productId = productId;
		this.testProductId = testProductId;
		this.consumable = consumable;
		this.titleId = titleId;
		this.descriptionId = descriptionId;
	}

	@Override
	public String getProductId() {
		return productId;
	}

	@Override
	public Integer getTitleId() {
		return titleId;
	}

	@Override
	public Integer getDescriptionId() {
		return descriptionId;
	}

	@Override
	public Boolean isConsumable() {
		return consumable;
	}

	@Override
	public ItemType getItemType() {
		return ItemType.MANAGED;
	}

	@Override
	public String getTestProductId() {
		return testProductId;
	}

}
