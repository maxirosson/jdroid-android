package com.jdroid.android.loading

import androidx.annotation.ColorRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.jdroid.android.R
import com.jdroid.android.fragment.FragmentIf

class SwipeRefreshLoading : FragmentLoading {

    @ColorRes
    var colorRes1: Int = R.color.jdroid_colorPrimary

    @ColorRes
    var colorRes2: Int = R.color.jdroid_accentColor

    @ColorRes
    var colorRes3: Int = R.color.jdroid_colorPrimaryDark

    @ColorRes
    var colorRes4: Int = R.color.jdroid_accentColorPressed

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onViewCreated(fragmentIf: FragmentIf) {
        swipeRefreshLayout = fragmentIf.findView(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(fragmentIf as SwipeRefreshLayout.OnRefreshListener)
    }

    override fun show(fragmentIf: FragmentIf) {
        swipeRefreshLayout.setColorSchemeResources(colorRes1, colorRes2, colorRes3, colorRes4)
        swipeRefreshLayout.isRefreshing = true
    }

    override fun dismiss(fragmentIf: FragmentIf) {
        swipeRefreshLayout.isRefreshing = false
    }
}
