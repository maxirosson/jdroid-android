package com.jdroid.android.firebase.admob.helpers

import android.app.Activity

import com.google.android.gms.ads.AdView

class AdViewHelper : BaseAdViewHelper() {

    override fun createBaseAdViewWrapper(activity: Activity): BaseAdViewWrapper {
        return BaseAdViewWrapper(AdView(activity))
    }
}
