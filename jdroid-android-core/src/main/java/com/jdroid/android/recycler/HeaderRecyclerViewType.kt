package com.jdroid.android.recycler

abstract class HeaderRecyclerViewType : ViewHolderlessRecyclerViewType<HeaderRecyclerViewType.HeaderItem>() {

    override fun getItemClass(): Class<HeaderItem> {
        return HeaderItem::class.java
    }

    override fun isClickable(): Boolean {
        return false
    }

    class HeaderItem
}