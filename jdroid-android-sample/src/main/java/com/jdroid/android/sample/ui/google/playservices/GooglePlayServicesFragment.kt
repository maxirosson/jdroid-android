package com.jdroid.android.sample.ui.google.playservices

import android.os.Bundle
import android.view.View

import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.google.GooglePlayServicesUtils
import com.jdroid.android.sample.R

class GooglePlayServicesFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.google_play_services_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.checkGooglePlayServices).setOnClickListener { GooglePlayServicesUtils.checkGooglePlayServices(activity!!) }

        findView<View>(R.id.openGooglePlayServices).setOnClickListener { GooglePlayServicesUtils.launchGooglePlayServicesUpdate() }
    }
}
