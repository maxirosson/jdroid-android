package com.jdroid.android.google.inappbilling.client

interface DeveloperPayloadVerificationStrategy {

    /*
	 *  Verifies the developer payload of a purchase.
	 */
    fun verify(product: Product): Boolean
}
