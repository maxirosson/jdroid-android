package com.jdroid.android.sample.ui.google.admob

import androidx.fragment.app.Fragment

import com.google.android.gms.ads.AdSize
import com.jdroid.android.activity.ActivityDelegate
import com.jdroid.android.activity.FragmentContainerActivity
import com.jdroid.android.application.AppModule
import com.jdroid.android.firebase.admob.AdMobActivityDelegate
import com.jdroid.android.firebase.admob.AdMobAppModule
import com.jdroid.android.firebase.admob.helpers.BaseAdViewHelper
import com.jdroid.android.sample.R
import com.jdroid.android.sample.application.AndroidAppContext

class ActivityBannerActivity : FragmentContainerActivity() {

    override fun getContentView(): Int {
        return R.layout.activity_banner_activity
    }

    override fun getFragmentClass(): Class<out Fragment> {
        return ActivityBannerFragment::class.java
    }

    override fun createActivityDelegate(appModule: AppModule): ActivityDelegate? {
        return if (appModule is AdMobAppModule) {
            object : AdMobActivityDelegate(this@ActivityBannerActivity) {

                override fun initBaseAdViewHelper(adHelper: BaseAdViewHelper) {
                    adHelper.adSize = AdSize.BANNER
                    adHelper.setAdUnitId(AndroidAppContext.SAMPLE_BANNER_AD_UNIT_ID)
                }
            }
        } else {
            super.createActivityDelegate(appModule)
        }
    }
}
