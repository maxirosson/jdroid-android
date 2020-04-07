package com.jdroid.android.sample.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView

import com.jdroid.android.recycler.AutofitGridLayoutManager
import com.jdroid.android.sample.R
import com.jdroid.android.utils.ScreenUtils

class AutofitGridLayoutRecyclerFragment : SimpleRecyclerFragment() {

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        return AutofitGridLayoutManager(activity, ScreenUtils.convertDimenToPixel(R.dimen.autoGridItemWidth))
    }
}
