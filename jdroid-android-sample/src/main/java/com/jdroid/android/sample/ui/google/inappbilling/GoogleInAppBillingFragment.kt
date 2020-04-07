package com.jdroid.android.sample.ui.google.inappbilling

import com.jdroid.android.google.inappbilling.client.Product
import com.jdroid.android.google.inappbilling.ui.InAppBillingRecyclerFragment
import com.jdroid.android.sample.R
import com.jdroid.android.utils.ToastUtils

class GoogleInAppBillingFragment : InAppBillingRecyclerFragment() {

    override fun onPurchased(product: Product) {
        getRecyclerViewAdapter().notifyDataSetChanged()
        ToastUtils.showToast(R.string.jdroid_purchaseThanks)
    }

    override fun onProvideProduct(product: Product) {
        // Do nothing
    }
}
