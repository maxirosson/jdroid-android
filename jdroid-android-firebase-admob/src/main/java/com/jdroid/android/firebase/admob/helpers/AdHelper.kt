package com.jdroid.android.firebase.admob.helpers

import android.app.Activity
import android.view.ViewGroup

import com.google.android.gms.ads.AdListener

interface AdHelper {

    fun setAdUnitId(adUnitId: String): AdHelper

    fun setAdListeners(adListeners: List<AdListener>): AdHelper

    fun loadAd(activity: Activity, adViewContainer: ViewGroup?)
}
