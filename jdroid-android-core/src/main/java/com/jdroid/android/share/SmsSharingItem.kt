package com.jdroid.android.share

import com.jdroid.android.utils.ExternalAppsUtils

class SmsSharingItem(sharingData: SharingData) : AppSharingItem(sharingData) {

    override fun getSharingMedium(): SharingMedium {
        return SharingMedium.SMS
    }

    override fun isEnabled(): Boolean {
        return super.isEnabled() && getApplicationId() != ExternalAppsUtils.HANGOUTS_PACKAGE_NAME
    }
}
