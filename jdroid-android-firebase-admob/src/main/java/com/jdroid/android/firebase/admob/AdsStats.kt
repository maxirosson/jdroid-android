package com.jdroid.android.firebase.admob

import com.jdroid.java.date.DateUtils

object AdsStats {

    private var lastInterstitialOpenedTimestamp: Long = 0L

    fun onInterstitialOpened() {
        lastInterstitialOpenedTimestamp = DateUtils.nowMillis()
    }

    fun getLastInterstitialOpenedTimestamp(): Long {
        return lastInterstitialOpenedTimestamp
    }
}
