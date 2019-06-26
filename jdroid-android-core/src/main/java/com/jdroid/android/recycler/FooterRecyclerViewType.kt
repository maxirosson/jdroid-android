package com.jdroid.android.recycler

abstract class FooterRecyclerViewType : ViewHolderlessRecyclerViewType<FooterRecyclerViewType.FooterItem>() {

    override fun getItemClass(): Class<FooterItem> {
        return FooterItem::class.java
    }

    override fun isClickable(): Boolean? {
        return false
    }

    class FooterItem
}