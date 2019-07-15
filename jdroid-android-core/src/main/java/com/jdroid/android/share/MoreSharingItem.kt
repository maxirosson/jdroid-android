package com.jdroid.android.share

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat

import com.jdroid.android.R
import com.jdroid.android.application.AbstractApplication

class MoreSharingItem(private val sharingData: SharingData) : SharingItem() {

    override fun share(activity: Activity) {
        ShareUtils.shareTextContent(activity, sharingData.shareKey, activity.getString(R.string.jdroid_share), sharingData.defaultSharingDataItem.subject, sharingData.defaultSharingDataItem.text)
    }

    override fun getAppIcon(): Drawable? {
        val drawable = DrawableCompat.wrap(AbstractApplication.get().resources.getDrawable(R.drawable.jdroid_more_selector))
        DrawableCompat.setTint(drawable, AbstractApplication.get().resources.getColor(R.color.jdroid_colorPrimary))
        return drawable
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getApplicationId(): String? {
        return null
    }
}
