package com.jdroid.android.sample.ui.navdrawer

import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R

class NoNavDrawerFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.no_navdrawer_fragment
    }
}
