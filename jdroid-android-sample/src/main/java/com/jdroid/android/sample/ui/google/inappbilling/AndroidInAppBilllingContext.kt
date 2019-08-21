package com.jdroid.android.sample.ui.google.inappbilling

import com.jdroid.android.google.inappbilling.InAppBillingContext
import com.jdroid.android.google.inappbilling.client.ProductType

class AndroidInAppBilllingContext : InAppBillingContext() {

    override fun getManagedProductTypes(): List<ProductType> {
        return listOf(*SampleProductType.values())
    }
}
