package com.jdroid.android.sample.ui.google.admob

import com.google.android.gms.ads.AdSize
import com.jdroid.android.application.AppModule
import com.jdroid.android.firebase.admob.AdMobAppModule
import com.jdroid.android.firebase.admob.AdMobFragmentDelegate
import com.jdroid.android.firebase.admob.helpers.BaseAdViewHelper
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.fragment.FragmentDelegate
import com.jdroid.android.sample.R
import com.jdroid.android.sample.application.AndroidAppContext

class FragmentBannerFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.empty_fragment
    }

    override fun createFragmentDelegate(appModule: AppModule): FragmentDelegate? {
        return if (appModule is AdMobAppModule) {
            object : AdMobFragmentDelegate(this) {
                override fun initBaseAdViewHelper(adHelper: BaseAdViewHelper) {
                    adHelper.adSize = AdSize.BANNER
                    adHelper.setAdUnitId(AndroidAppContext.SAMPLE_BANNER_AD_UNIT_ID)
                }
            }
        } else {
            super.createFragmentDelegate(appModule)
        }
    }
}
