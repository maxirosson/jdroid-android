package com.jdroid.android.sample.ui.leakcanary

import android.os.Bundle
import android.view.View

import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R

class LeakCanaryFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.leakcanary_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LEAK = this
    }

    companion object {
        var LEAK: AbstractFragment? = null
    }
}
