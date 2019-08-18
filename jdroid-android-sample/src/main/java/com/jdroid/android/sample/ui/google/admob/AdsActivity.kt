package com.jdroid.android.sample.ui.google.admob

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.ActivityDelegate
import com.jdroid.android.activity.FragmentContainerActivity
import com.jdroid.android.application.AppModule
import com.jdroid.android.firebase.admob.AdMobActivityDelegate
import com.jdroid.android.firebase.admob.AdMobAppModule
import com.jdroid.android.firebase.admob.helpers.InterstitialAdHelper
import com.jdroid.android.sample.application.AndroidAppContext

class AdsActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return AdsFragment::class.java
    }

    override fun createActivityDelegate(appModule: AppModule): ActivityDelegate? {
        return if (appModule is AdMobAppModule) {
            object : AdMobActivityDelegate(this@AdsActivity) {

                override fun createInterstitialAdHelper(): InterstitialAdHelper? {
                    return InterstitialAdHelper()
                }

                override fun initInterstitialAdHelper(adHelper: InterstitialAdHelper) {
                    adHelper.setAdUnitId(AndroidAppContext.SAMPLE_INTERSTITIAL_AD_UNIT_ID)
                }
            }
        } else {
            super.createActivityDelegate(appModule)
        }
    }
}
