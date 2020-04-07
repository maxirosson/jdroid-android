package com.jdroid.android.firebase.admob

import android.os.Bundle

import com.google.android.gms.ads.MobileAds
import com.jdroid.android.activity.AbstractFragmentActivity
import com.jdroid.android.activity.ActivityDelegate
import com.jdroid.android.firebase.admob.helpers.AdViewHelper
import com.jdroid.android.firebase.admob.helpers.BaseAdViewHelper
import com.jdroid.android.firebase.admob.helpers.InterstitialAdHelper
import com.jdroid.java.exception.UnexpectedException

open class AdMobActivityDelegate(activity: AbstractFragmentActivity) : ActivityDelegate(activity) {

    var baseAdViewHelper: BaseAdViewHelper? = null
        private set
    var interstitialAdHelper: InterstitialAdHelper? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        if (AdMobAppModule.adMobAppContext.areAdsEnabled()) {

            if (!initialized) {
                val adMobAppId = AdMobAppModule.adMobAppContext.getAdMobAppId() ?: throw UnexpectedException("Missing AdMob App Id")
                MobileAds.initialize(activity.applicationContext, adMobAppId)
                initialized = true
            }

            baseAdViewHelper = createBaseAdViewHelper()
            if (baseAdViewHelper != null) {
                initBaseAdViewHelper(baseAdViewHelper!!)
                baseAdViewHelper!!.loadAd(activity, activity.findViewById(R.id.adViewContainer))
            }

            interstitialAdHelper = createInterstitialAdHelper()
            if (interstitialAdHelper != null) {
                initInterstitialAdHelper(interstitialAdHelper!!)
                interstitialAdHelper!!.loadAd(activity, null)
            }
        }
    }

    override fun onResume() {
        baseAdViewHelper?.onResume()
    }

    override fun onBeforePause() {
        baseAdViewHelper?.onBeforePause()
    }

    override fun onBeforeDestroy() {
        baseAdViewHelper?.onBeforeDestroy()
    }

    fun createBaseAdViewHelper(): BaseAdViewHelper? {
        return AdViewHelper()
    }

    open fun createInterstitialAdHelper(): InterstitialAdHelper? {
        return null
    }

    open fun initBaseAdViewHelper(adHelper: BaseAdViewHelper) {
        // Do nothing
    }

    open fun initInterstitialAdHelper(adHelper: InterstitialAdHelper) {
        // Do nothing
    }

    companion object {
        private var initialized: Boolean = false
    }
}
