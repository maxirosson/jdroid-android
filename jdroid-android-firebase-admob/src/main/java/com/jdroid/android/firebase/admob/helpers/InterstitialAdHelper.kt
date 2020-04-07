package com.jdroid.android.firebase.admob.helpers

import android.app.Activity
import android.view.ViewGroup

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.jdroid.android.firebase.admob.AdMobAppModule
import com.jdroid.android.firebase.admob.AdsStats
import com.jdroid.android.location.LocationHelper
import com.jdroid.android.utils.AppUtils
import com.jdroid.java.exception.UnexpectedException

class InterstitialAdHelper : AdHelper {

    private var interstitial: InterstitialAd? = null
    private var displayInterstitial: Boolean = false
    private var interstitialAdUnitId: String? = null
    private var adListeners: List<AdListener>? = null

    init {
        interstitialAdUnitId = AdMobAppModule.adMobAppContext.getDefaultAdUnitId()
    }

    private fun createBuilder(): AdRequest.Builder {
        val builder = AdRequest.Builder()
        if (!AppUtils.isReleaseBuildType()) {
            builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            for (deviceId in AdMobAppModule.adMobAppContext.getTestDevicesIds()) {
                builder.addTestDevice(deviceId)
            }
        }
        builder.setLocation(LocationHelper.get().location)
        return builder
    }

    override fun loadAd(activity: Activity, adViewContainer: ViewGroup?) {
        if (AdMobAppModule.adMobAppContext.isInterstitialEnabled()) {
            interstitial = InterstitialAd(activity)

            if (interstitialAdUnitId == null) {
                throw UnexpectedException("Missing interstitial ad unit ID")
            }

            if (!AppUtils.isReleaseBuildType() && AdMobAppModule.adMobAppContext.isTestAdUnitIdEnabled()) {
                interstitial!!.adUnitId = TEST_AD_UNIT_ID
            } else {
                interstitial!!.adUnitId = interstitialAdUnitId
            }

            val builder = createBuilder()
            interstitial!!.loadAd(builder.build())

            val adListenerWrapper = AdListenerWrapper()
            adListenerWrapper.addAdListener(object : AdListener() {

                override fun onAdLoaded() {
                    if (displayInterstitial) {
                        displayInterstitial(false)
                    }
                }

                override fun onAdOpened() {
                    AdsStats.onInterstitialOpened()
                }
            })
            for (adListener in adListeners.orEmpty()) {
                adListenerWrapper.addAdListener(adListener)
            }
            interstitial!!.adListener = adListenerWrapper
        }
    }

    @JvmOverloads
    fun displayInterstitial(retryIfNotLoaded: Boolean = false, adListener: AdListener? = null): Boolean {
        displayInterstitial = retryIfNotLoaded
        if (interstitial != null && interstitial!!.isLoaded) {
            if (adListener != null) {
                (interstitial!!.adListener as AdListenerWrapper).addAdListener(adListener)
            }
            interstitial!!.show()
            displayInterstitial = false
            return true
        }
        return false
    }

    override fun setAdUnitId(adUnitId: String): AdHelper {
        this.interstitialAdUnitId = adUnitId
        return this
    }

    override fun setAdListeners(adListeners: List<AdListener>): AdHelper {
        this.adListeners = adListeners
        return this
    }

    companion object {
        private const val TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
    }
}
