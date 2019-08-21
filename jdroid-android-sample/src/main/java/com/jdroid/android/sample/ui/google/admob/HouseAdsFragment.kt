package com.jdroid.android.sample.ui.google.admob

import android.app.Activity
import android.view.View
import android.widget.TextView

import com.google.android.gms.ads.AdSize
import com.jdroid.android.application.AppModule
import com.jdroid.android.firebase.admob.AdMobAppModule
import com.jdroid.android.firebase.admob.AdMobFragmentDelegate
import com.jdroid.android.firebase.admob.HouseAdBuilder
import com.jdroid.android.firebase.admob.helpers.BaseAdViewHelper
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.fragment.FragmentDelegate
import com.jdroid.android.sample.R

class HouseAdsFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.house_ads_fragment
    }

    override fun createFragmentDelegate(appModule: AppModule): FragmentDelegate? {
        return if (appModule is AdMobAppModule) {
            object : AdMobFragmentDelegate(this) {
                override fun initBaseAdViewHelper(adHelper: BaseAdViewHelper) {
                    adHelper.adSize = AdSize.BANNER
                    adHelper.houseAdBuilder = object : HouseAdBuilder() {
                        override fun build(activity: Activity): View? {
                            var view = super.build(activity)
                            if (view == null) {
                                view = TextView(getActivity())
                                view.setText(R.string.houseAd)
                            }
                            return view
                        }
                    }
                }
            }
        } else {
            super.createFragmentDelegate(appModule)
        }
    }
}
