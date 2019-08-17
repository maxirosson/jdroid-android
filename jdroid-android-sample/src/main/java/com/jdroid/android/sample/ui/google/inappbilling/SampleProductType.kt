package com.jdroid.android.sample.ui.google.inappbilling

import com.jdroid.android.google.inappbilling.client.ItemType
import com.jdroid.android.google.inappbilling.client.ProductType
import com.jdroid.android.google.inappbilling.client.TestProductType
import com.jdroid.android.sample.R

enum class SampleProductType private constructor(
    // 	NOT_CONSUMABLE_PURCHASED("sample.not.consumable.purchased.id", TestProductType.PURCHASED, false, null, null),
    // 	NOT_CONSUMABLE_CANCELED("sample.not.consumable.canceled.id", TestProductType.CANCELED, false, null, null),
    // 	NOT_CONSUMABLE_REFUNDED("sample.not.consumable.refunded.id", TestProductType.REFUNDED, false, null, null),
    // 	NOT_CONSUMABLE_UNAVAILABLE("sample.not.consumable.unavailable.id", TestProductType.UNAVAILABLE, false, null, null);

    private val productId: String,
    private val testProductId: String,
    private val consumable: Boolean,
    private val titleId: Int?,
    private val descriptionId: Int?
) : ProductType {

    CONSUMABLE_PURCHASED("sample.consumable.purchased.id", TestProductType.PURCHASED, true, null, null),
    CONSUMABLE_CANCELED("sample.consumable.canceled.id", TestProductType.CANCELED, true, null, null),
    CONSUMABLE_UNAVAILABLED("sample.consumable.unavailable.id", TestProductType.UNAVAILABLE, true, null, null) {
        override fun getIconId(): Int? {
            return null
        }
    };

    override fun getProductId(): String {
        return productId
    }

    override fun getTitleId(): Int? {
        return titleId
    }

    override fun getDescriptionId(): Int? {
        return descriptionId
    }

    override fun isConsumable(): Boolean {
        return consumable
    }

    override fun getItemType(): ItemType {
        return ItemType.MANAGED
    }

    override fun getTestProductId(): String {
        return testProductId
    }

    override fun getIconId(): Int? {
        return R.drawable.ic_inapp_billing
    }
}
