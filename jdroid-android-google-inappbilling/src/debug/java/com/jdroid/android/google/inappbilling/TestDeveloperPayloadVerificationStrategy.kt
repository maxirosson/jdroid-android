package com.jdroid.android.google.inappbilling

import com.jdroid.android.google.inappbilling.client.DeveloperPayloadVerificationStrategy
import com.jdroid.android.google.inappbilling.client.Product

class TestDeveloperPayloadVerificationStrategy : DeveloperPayloadVerificationStrategy {

    override fun verify(product: Product): Boolean {
        return true
    }

    companion object {
        const val VALID_TEST_VERIFICATION_ENABLED = "validTestVerificationEnabled"
    }
}
