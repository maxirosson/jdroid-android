package com.jdroid.android.firebase.admob

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jdroid.android.firebase.admob.helpers.AdViewHelper
import com.jdroid.android.firebase.admob.helpers.BaseAdViewHelper
import com.jdroid.android.fragment.FragmentDelegate

open class AdMobFragmentDelegate(fragment: Fragment) : FragmentDelegate(fragment) {

    var baseAdViewHelper: BaseAdViewHelper? = null
        private set

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (AdMobAppModule.adMobAppContext.areAdsEnabled()) {
            baseAdViewHelper = createBaseAdViewHelper()
            if (baseAdViewHelper != null) {
                initBaseAdViewHelper(baseAdViewHelper!!)
                baseAdViewHelper!!.loadAd(fragment.requireActivity(), view.findViewById(getAdViewContainerId()))
            }
        }
    }

    override fun onResume() {
        baseAdViewHelper?.onResume()
    }

    override fun onBeforePause() {
        baseAdViewHelper?.onBeforePause()
    }

    override fun onBeforeDestroy() {
        baseAdViewHelper?.onBeforeDestroy()
    }

    protected fun getAdViewContainerId(): Int {
        return R.id.adViewContainer
    }

    fun createBaseAdViewHelper(): BaseAdViewHelper? {
        return AdViewHelper()
    }

    open fun initBaseAdViewHelper(adHelper: BaseAdViewHelper) {
        // Do nothing
    }
}
