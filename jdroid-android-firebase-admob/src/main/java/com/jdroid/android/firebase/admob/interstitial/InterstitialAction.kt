package com.jdroid.android.firebase.admob.interstitial

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.jdroid.android.activity.ActivityIf
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.firebase.admob.AdMobActivityDelegate
import com.jdroid.android.firebase.admob.AdMobAppModule
import com.jdroid.java.remoteconfig.RemoteConfigParameter

abstract class InterstitialAction {

    fun start(context: Context) {
        start(context as ActivityIf)
    }

    fun start(activityIf: ActivityIf) {
        val enabled = AbstractApplication.get().remoteConfigLoader!!.getBoolean(getEnabledRemoteConfigParameter())!!
        if (enabled) {
            val interstitialAdHelper = (activityIf.getActivityDelegate(AdMobAppModule.get()) as AdMobActivityDelegate).interstitialAdHelper
            if (interstitialAdHelper != null) {
                val displayed = interstitialAdHelper.displayInterstitial(false, object : AdListener() {
                    override fun onAdClosed() {
                        onAction()
                    }
                })
                if (!displayed) {
                    onAction()
                }
            } else {
                onAction()
            }
        } else {
            onAction()
        }
    }

    protected abstract fun getEnabledRemoteConfigParameter(): RemoteConfigParameter

    protected abstract fun onAction()
}
