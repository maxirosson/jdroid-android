package com.jdroid.android.google.inappbilling.client

import com.android.billingclient.api.BillingClient

enum class ItemType constructor(
    @BillingClient.SkuType
    val type: String
) {

    MANAGED("inapp"),
    SUBSCRIPTION("subs")
}
