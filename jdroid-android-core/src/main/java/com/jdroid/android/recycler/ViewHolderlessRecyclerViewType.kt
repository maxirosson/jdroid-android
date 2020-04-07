package com.jdroid.android.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolderlessRecyclerViewType<ITEM> : RecyclerViewType<ITEM, EmptyViewHolder>() {

    override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
        return EmptyViewHolder(view)
    }

    override fun fillHolderFromItem(item: ITEM, holder: EmptyViewHolder) {
        // Do nothing
    }
}
