package com.jdroid.android.loading

import com.jdroid.android.R
import com.jdroid.android.fragment.FragmentIf

class NonBlockingLoading : FragmentLoading {

    var isNonBlockingLoadingDisplayedByDefault: Boolean = true

    override fun onViewCreated(fragmentIf: FragmentIf) {
        // Do nothing
    }

    override fun show(fragmentIf: FragmentIf) {
        val loadingLayout = fragmentIf.findView<LoadingLayout>(R.id.loadingLayout)
        loadingLayout.setLoading(isNonBlockingLoadingDisplayedByDefault)
        loadingLayout.showLoading(fragmentIf)
    }

    override fun dismiss(fragmentIf: FragmentIf) {
        val loadingLayout = fragmentIf.findView<LoadingLayout>(R.id.loadingLayout)
        loadingLayout.dismissLoading(fragmentIf)
    }
}
