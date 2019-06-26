package com.jdroid.android.recycler

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.jdroid.android.R
import com.jdroid.android.loading.FragmentLoading
import com.jdroid.android.loading.SwipeRefreshLoading

open class SwipeRecyclerFragment : AbstractRecyclerFragment(), SwipeRefreshLayout.OnRefreshListener {

    override fun getContentFragmentLayout(): Int? {
        return if (isCardViewDecorationEnabled) R.layout.jdroid_cardview_swipe_recycler_fragment else R.layout.jdroid_swipe_recycler_fragment
    }

    override fun getDefaultLoading(): FragmentLoading? {
        return SwipeRefreshLoading()
    }

    override fun onRefresh() {
        // Do Nothing
    }
}
