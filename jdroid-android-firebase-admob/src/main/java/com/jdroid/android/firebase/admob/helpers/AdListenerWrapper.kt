package com.jdroid.android.firebase.admob.helpers

import com.google.android.gms.ads.AdListener
import com.jdroid.java.utils.LoggerUtils

class AdListenerWrapper : AdListener() {

    private val LOGGER = LoggerUtils.getLogger(AdListenerWrapper::class.java)

    private val wrappedAdListeners = mutableListOf<AdListener>()
    private val transientAdListeners = mutableListOf<AdListener>()

    override fun onAdLoaded() {
        LOGGER.info("Ad loaded")
        for (adListener in wrappedAdListeners) {
            adListener.onAdLoaded()
        }
    }

    override fun onAdFailedToLoad(i: Int) {
        LOGGER.info("Ad failed to load: $i")
        for (adListener in wrappedAdListeners) {
            adListener.onAdFailedToLoad(i)
        }
    }

    override fun onAdOpened() {
        LOGGER.info("Ad opened")
        for (adListener in wrappedAdListeners) {
            adListener.onAdOpened()
        }
    }

    override fun onAdImpression() {
        LOGGER.info("Ad impression")
        for (adListener in wrappedAdListeners) {
            adListener.onAdImpression()
        }
    }

    override fun onAdClicked() {
        LOGGER.info("Ad clicked")
        for (adListener in wrappedAdListeners) {
            adListener.onAdClicked()
        }
    }

    override fun onAdClosed() {
        LOGGER.info("Ad closed")
        for (adListener in wrappedAdListeners) {
            adListener.onAdClosed()
            transientAdListeners.remove(adListener)
        }
    }

    override fun onAdLeftApplication() {
        LOGGER.info("Ad left application")
        for (adListener in wrappedAdListeners) {
            adListener.onAdLeftApplication()
        }
    }

    fun addAdListener(adListener: AdListener) {
        this.wrappedAdListeners.add(adListener)
    }

    fun addTransientAdListener(adListener: AdListener) {
        this.wrappedAdListeners.add(adListener)
        this.transientAdListeners.add(adListener)
    }
}
