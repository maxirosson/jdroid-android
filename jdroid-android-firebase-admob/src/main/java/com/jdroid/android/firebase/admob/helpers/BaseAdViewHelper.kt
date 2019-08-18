package com.jdroid.android.firebase.admob.helpers

import android.app.Activity
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.firebase.admob.AdMobAppModule
import com.jdroid.android.firebase.admob.HouseAdBuilder
import com.jdroid.android.location.LocationHelper
import com.jdroid.android.utils.AppUtils

abstract class BaseAdViewHelper : AdHelper {

    private var baseAdViewWrapper: BaseAdViewWrapper? = null
    private var adViewContainer: ViewGroup? = null
    var adSize: AdSize? = null
    private var adUnitId: String? = null
    var houseAdBuilder: HouseAdBuilder? = null

    private var adListeners: List<AdListener>? = null

    private var displayAds: Boolean = false

    init {
        adUnitId = AdMobAppModule.adMobAppContext.getDefaultAdUnitId()
    }

    override fun loadAd(activity: Activity, adViewContainer: ViewGroup?) {
        if (adViewContainer != null) {
            if (adSize != null && AdMobAppModule.adMobAppContext.areAdsEnabled()) {
                if (adUnitId != null) {

                    baseAdViewWrapper = createBaseAdViewWrapper(activity)

                    if (!AppUtils.isReleaseBuildType() && AdMobAppModule.adMobAppContext.isTestAdUnitIdEnabled()) {
                        baseAdViewWrapper!!.setAdUnitId(TEST_AD_UNIT_ID)
                    } else {
                        baseAdViewWrapper!!.setAdUnitId(adUnitId!!)
                    }
                    baseAdViewWrapper!!.setAdSize(adSize!!)
                    val customView = if (houseAdBuilder != null) houseAdBuilder!!.build(activity) else null

                    val adListenerWrapper = AdListenerWrapper()
                    if (customView != null) {

                        adViewContainer.visibility = View.VISIBLE
                        adViewContainer.addView(customView)

                        adListenerWrapper.addAdListener(object : AdListener() {

                            override fun onAdLoaded() {
                                if (displayAds) {
                                    baseAdViewWrapper!!.baseAdView.visibility = View.VISIBLE
                                    customView.visibility = View.GONE
                                } else {
                                    baseAdViewWrapper!!.baseAdView.postDelayed({
                                        displayAds = true
                                        if (baseAdViewWrapper != null) {
                                            baseAdViewWrapper!!.baseAdView.visibility = View.VISIBLE
                                            customView.visibility = View.GONE
                                        }
                                    }, DateUtils.SECOND_IN_MILLIS * 10)
                                }
                            }

                            override fun onAdClosed() {
                                baseAdViewWrapper!!.baseAdView.visibility = View.GONE
                                customView.visibility = View.VISIBLE
                            }

                            override fun onAdFailedToLoad(errorCode: Int) {
                                baseAdViewWrapper!!.baseAdView.visibility = View.GONE
                                customView.visibility = View.VISIBLE
                            }
                        })
                    } else {
                        adListenerWrapper.addAdListener(object : AdListener() {

                            override fun onAdLoaded() {
                                adViewContainer.visibility = View.VISIBLE
                                baseAdViewWrapper!!.baseAdView.visibility = View.VISIBLE
                            }

                            override fun onAdClosed() {
                                adViewContainer.visibility = View.GONE
                                baseAdViewWrapper!!.baseAdView.visibility = View.GONE
                            }

                            override fun onAdFailedToLoad(errorCode: Int) {
                                adViewContainer.visibility = View.GONE
                                baseAdViewWrapper!!.baseAdView.visibility = View.GONE
                            }
                        })
                    }

                    for (adListener in adListeners.orEmpty()) {
                        adListenerWrapper.addAdListener(adListener)
                    }
                    baseAdViewWrapper!!.setAdListener(adListenerWrapper)

                    val builder = createBuilder()
                    try {
                        baseAdViewWrapper!!.loadAd(builder.build())
                        adViewContainer.addView(baseAdViewWrapper!!.baseAdView)
                    } catch (e: Exception) {
                        AbstractApplication.get().exceptionHandler.logWarningException("Error when loading ad", e)
                    }
                } else {
                    AbstractApplication.get().exceptionHandler.logWarningException("Missing ad unit ID on activity " + activity.javaClass.simpleName)
                }
            } else {
                adViewContainer.visibility = View.GONE
            }
        }
    }

    protected abstract fun createBaseAdViewWrapper(activity: Activity): BaseAdViewWrapper

    protected open fun createBuilder(): AdRequest.Builder {
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

    override fun setAdListeners(adListeners: List<AdListener>): AdHelper {
        this.adListeners = adListeners
        return this
    }

    fun onBeforePause() {
        baseAdViewWrapper?.pause()
    }

    fun onResume() {
        if (baseAdViewWrapper != null) {
            if (AdMobAppModule.adMobAppContext.areAdsEnabled()) {
                baseAdViewWrapper!!.resume()
            } else if (adViewContainer != null) {
                adViewContainer!!.removeView(baseAdViewWrapper!!.baseAdView)
                adViewContainer!!.visibility = View.GONE
                baseAdViewWrapper = null
                adViewContainer = null
            }
        }
    }

    override fun setAdUnitId(adUnitId: String): AdHelper {
        this.adUnitId = adUnitId
        return this
    }

    fun onBeforeDestroy() {
        baseAdViewWrapper?.destroy()
        baseAdViewWrapper = null
        adViewContainer = null
    }

    companion object {
        const val TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    }
}
