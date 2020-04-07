package com.jdroid.android.firebase.admob.helpers

import android.view.View

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class BaseAdViewWrapper(val baseAdView: View) {

    fun setAdUnitId(adUnitId: String) {
        (baseAdView as AdView).adUnitId = adUnitId
    }

    fun setAdSize(adSize: AdSize) {
        (baseAdView as AdView).adSize = adSize
    }

    fun setAdListener(adListener: AdListener) {
        (baseAdView as AdView).adListener = adListener
    }

    fun loadAd(adRequest: AdRequest) {
        (baseAdView as AdView).loadAd(adRequest)
    }

    fun pause() {
        (baseAdView as AdView).pause()
    }

    fun resume() {
        (baseAdView as AdView).resume()
    }

    fun destroy() {
        (baseAdView as AdView).destroy()
    }
}
