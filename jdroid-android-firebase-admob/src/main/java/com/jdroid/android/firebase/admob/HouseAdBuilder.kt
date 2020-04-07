package com.jdroid.android.firebase.admob

import android.app.Activity
import android.view.View
import android.view.View.OnClickListener

open class HouseAdBuilder {

    private var removeAdsClickListener: OnClickListener? = null

    open fun build(activity: Activity): View? {
        var view: View? = null
        if (RemoveAdsView.displayRemoveAdsView() && removeAdsClickListener != null) {
            view = RemoveAdsView(activity)
            view.setOnClickListener(removeAdsClickListener)
        }
        return view
    }

    fun setRemoveAdsClickListener(removeAdsClickListener: OnClickListener) {
        this.removeAdsClickListener = removeAdsClickListener
    }
}
