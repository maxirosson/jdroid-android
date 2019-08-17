package com.jdroid.android.google.inappbilling.client

interface ProductType {

    /*
	 * @return A unique, human readable ID for your product. Product IDs are also called SKUs
	 */
    fun getProductId(): String

    /*
	 * @return The product ID for the product used when the static responses is enabled
	 */
    fun getTestProductId(): String

    /*
	 * Non-consumable Items. Once purchased, these items will be permanently associated to the user's Google account.
	 * A one-time product that can be used indefinitely is called a non-consumable.
	 * An example of a non-consumable in-app product is a premium upgrade or a level pack.
	 *
	 * Consumable items. Items that can be made available for purchase multiple times.
	 * A one-time product with non-infinite use is called a consumable.
	 */
    fun isConsumable(): Boolean

    fun getItemType(): ItemType

    fun getTitleId(): Int?

    fun getDescriptionId(): Int?

    fun getIconId(): Int?
}
