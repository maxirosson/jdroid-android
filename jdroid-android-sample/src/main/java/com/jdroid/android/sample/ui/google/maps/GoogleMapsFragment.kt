package com.jdroid.android.sample.ui.google.maps

import android.os.Bundle
import android.view.View
import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R

class GoogleMapsFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.google_maps_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.fullMap).setOnClickListener { ActivityLauncher.startActivity(activity, MapActivity::class.java) }

        findView<View>(R.id.liteModeMap).setOnClickListener { ActivityLauncher.startActivity(activity, LiteModeMapActivity::class.java) }

        findView<View>(R.id.streetView).setOnClickListener { ActivityLauncher.startActivity(activity, StreetViewActivity::class.java) }
    }
}
