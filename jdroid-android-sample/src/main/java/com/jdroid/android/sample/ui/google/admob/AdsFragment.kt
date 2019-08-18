package com.jdroid.android.sample.ui.google.admob

import android.os.Bundle
import android.view.View
import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.firebase.admob.interstitial.InterstitialAction
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.sample.firebase.remoteconfig.AndroidRemoteConfigParameter
import com.jdroid.android.utils.ToastUtils
import com.jdroid.java.remoteconfig.RemoteConfigParameter

class AdsFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.ads_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.fragmentBanner).setOnClickListener { ActivityLauncher.startActivity(activity, FragmentBannerActivity::class.java) }

        findView<View>(R.id.adReycler).setOnClickListener { ActivityLauncher.startActivity(activity, AdRecyclerActivity::class.java) }

        findView<View>(R.id.activityBanner).setOnClickListener { ActivityLauncher.startActivity(activity, ActivityBannerActivity::class.java) }

        findView<View>(R.id.displayInterstitial).setOnClickListener {
            object : InterstitialAction() {
                override fun onAction() {
                    ToastUtils.showToast(R.string.interstitialAction)
                }

                override fun getEnabledRemoteConfigParameter(): RemoteConfigParameter {
                    return AndroidRemoteConfigParameter.INTERSTITIAL_ENABLED
                }
            }.start(getActivityIf()!!)
        }

        findView<View>(R.id.houseAds).setOnClickListener { ActivityLauncher.startActivity(activity, HouseAdsActivity::class.java) }
    }
}
