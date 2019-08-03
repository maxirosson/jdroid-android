package com.jdroid.android.sample.ui.webview

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class WebViewActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return WebViewFragment::class.java
    }
}