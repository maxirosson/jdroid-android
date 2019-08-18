package com.jdroid.android.firebase.admob

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdroid.android.firebase.admob.helpers.BaseAdViewHelper
import com.jdroid.android.recycler.RecyclerViewType

abstract class AdViewType : RecyclerViewType<BaseAdViewHelper, AdViewType.AdHolder>() {

    class AdHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var adViewContainer: ViewGroup
    }

    override fun getItemClass(): Class<BaseAdViewHelper> {
        return BaseAdViewHelper::class.java
    }

    override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
        val holder = AdHolder(view)
        holder.adViewContainer = findView(view, R.id.jdroid_recyclerAdViewContainer)
        return holder
    }

    override fun fillHolderFromItem(baseAdViewHelper: BaseAdViewHelper, holder: AdHolder) {
        if (holder.adViewContainer.childCount == 0) {
            baseAdViewHelper.loadAd(activity, holder.adViewContainer)
        }
    }
}