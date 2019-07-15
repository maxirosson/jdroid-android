package com.jdroid.android.share

import android.app.Activity

abstract class AppSharingItem(private val sharingData: SharingData) : SharingItem() {

    abstract fun getSharingMedium(): SharingMedium

    override fun share(activity: Activity) {
        var sharingDataItem = sharingData.shareInfoItemMap[getSharingMedium().getMediumName()]
        if (sharingDataItem == null) {
            sharingDataItem = sharingData.defaultSharingDataItem
        }

        var text = if (sharingDataItem!!.text != null) sharingDataItem.text else sharingData.defaultSharingDataItem.text
        val link = if (sharingDataItem.link != null) sharingDataItem.link else sharingData.defaultSharingDataItem.link
        if (link != null) {
            text = text.replace("\${link}", link)
        }
        ShareUtils.share(activity, getSharingMedium(), sharingData.shareKey, text)
    }

    override fun getApplicationId(): String? {
        return getSharingMedium().applicationId
    }
}
