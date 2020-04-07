package com.jdroid.android.share

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView

import com.jdroid.android.R
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.utils.ScreenUtils

class ShareView : FrameLayout {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    fun init(activity: Activity, sharingItem: SharingItem) {
        LayoutInflater.from(activity).inflate(R.layout.jdroid_share_view, this, true)
        (findViewById<View>(R.id.shareAppIcon) as ImageView).setImageDrawable(sharingItem.getAppIcon())
        setOnClickListener { sharingItem.share(activity) }
    }

    companion object {

        fun initShareSection(activity: Activity, sharingItems: List<SharingItem>, moreSharingItem: MoreSharingItem?): Boolean {
            val shareItemsContainer = activity.findViewById<ViewGroup>(R.id.shareItemsContainer)
            val result = initShareSection(activity, sharingItems)

            if (moreSharingItem != null && result) {
                val shareView = ShareView(activity)
                shareView.init(activity, moreSharingItem)
                shareItemsContainer.addView(shareView)
            }
            return result
        }

        fun initShareSectionV2(activity: Activity, sharingItems: List<SharingItem>, sharingData: SharingData): Boolean {
            val result = initShareSection(activity, sharingItems)
            activity.findViewById<View>(R.id.shareMore).setOnClickListener {
                ShareUtils.shareTextContent(activity, sharingData.shareKey, AbstractApplication.get().getString(R.string.jdroid_share), sharingData.defaultSharingDataItem.subject, sharingData.defaultSharingDataItem.text)
            }
            return result
        }

        private fun initShareSection(activity: Activity, sharingItems: List<SharingItem>): Boolean {
            val shareSection = activity.findViewById<ViewGroup>(R.id.shareSection)
            val shareItemsContainer = activity.findViewById<ViewGroup>(R.id.shareItemsContainer)
            val itemWidth = ScreenUtils.convertDimenToPixel(R.dimen.jdroid_shareItemWidth)
            var itemsToDisplay = ScreenUtils.getScreenWidthPx() / itemWidth - 1
            for (each in sharingItems) {
                if (each.isEnabled() && itemsToDisplay > 0) {
                    val shareView = ShareView(activity)
                    shareView.init(activity, each)
                    shareItemsContainer.addView(shareView)
                    shareSection.visibility = View.VISIBLE
                    itemsToDisplay--
                }
            }
            return shareSection.visibility == View.VISIBLE
        }
    }
}
