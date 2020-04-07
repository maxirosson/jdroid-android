package com.jdroid.android.google.inappbilling.client

import androidx.annotation.MainThread

import com.jdroid.java.exception.AbstractException
import com.jdroid.java.exception.ErrorCodeException

interface InAppBillingClientListener {

    /**
     * Called to notify that setup is complete.
     */
    @MainThread
    fun onSetupFinished()

    /**
     * Called to notify that setup failed.
     *
     * @param abstractException The result of the setup process.
     */
    @MainThread
    fun onSetupFailed(abstractException: AbstractException)

    /**
     * Called to notify that a product details query operation completed.
     *
     * @param inventory The inventory.
     */
    @MainThread
    fun onQueryProductDetailsFinished(inventory: Inventory)

    /**
     * Called to notify that a product details query operation failed.
     *
     * @param errorCodeException The result of the operation.
     */
    @MainThread
    fun onQueryProductDetailsFailed(errorCodeException: ErrorCodeException)

    /**
     * Called to notify that a purchases query operation completed.
     *
     * @param inventory The inventory.
     */
    @MainThread
    fun onQueryPurchasesFinished(inventory: Inventory)

    /**
     * Called to notify that a purchases query operation failed.
     *
     * @param errorCodeException The result of the operation.
     */
    @MainThread
    fun onQueryPurchasesFailed(errorCodeException: ErrorCodeException)

    /**
     * Called to notify that an in-app purchase finished.
     *
     * @param product The [Product] purchased
     */
    @MainThread
    fun onPurchaseFinished(product: Product)

    /**
     * Called to notify that an in-app purchase failed.
     *
     * @param errorCodeException The result of the purchase.
     */
    @MainThread
    fun onPurchaseFailed(errorCodeException: ErrorCodeException)

    /**
     * Called to notify that a consumption has finished.
     *
     * @param product The [Product] that was (or was to be) consumed.
     */
    @MainThread
    fun onConsumeFinished(product: Product)

    /**
     * Called to notify that a consumption has failed.
     *
     * @param errorCodeException The result of the consumption operation.
     */
    @MainThread
    fun onConsumeFailed(errorCodeException: ErrorCodeException)

    @MainThread
    fun onProvideProduct(product: Product)
}
