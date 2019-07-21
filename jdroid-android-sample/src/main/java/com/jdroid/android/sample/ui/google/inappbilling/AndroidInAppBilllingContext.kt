package com.jdroid.android.sample.ui.google.inappbilling

import com.jdroid.android.google.inappbilling.InAppBillingContext
import com.jdroid.android.google.inappbilling.client.ProductType
import com.jdroid.java.collections.Lists

class AndroidInAppBilllingContext : InAppBillingContext() {

    override fun getManagedProductTypes(): List<ProductType> {
        return Lists.newArrayList(*SampleProductType.values())
    }
}
