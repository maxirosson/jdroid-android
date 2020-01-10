package com.jdroid.android.sample.ui.uri

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity
import com.jdroid.android.uri.UriHandler

class UriMapperSingleTopActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return CommonUriMapperFragment::class.java
    }

    override fun createUriHandler(): UriHandler<*>? {
        return UriMapperSingleTopUriHandler()
    }
}
