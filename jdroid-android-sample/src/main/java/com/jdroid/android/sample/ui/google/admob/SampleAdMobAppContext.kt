package com.jdroid.android.sample.ui.google.admob

import com.jdroid.android.firebase.admob.AdMobAppContext

class SampleAdMobAppContext : AdMobAppContext() {

    override fun areAdsEnabled(): Boolean {
        return true
    }
}
