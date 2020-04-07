package com.jdroid.android.sample.ui.google.inappbilling

import com.jdroid.android.google.inappbilling.InAppBillingAppModule
import com.jdroid.android.google.inappbilling.InAppBillingContext

class AndroidInAppBillingAppModule : InAppBillingAppModule() {

    override fun createInAppBillingContext(): InAppBillingContext {
        return AndroidInAppBilllingContext()
    }
}
