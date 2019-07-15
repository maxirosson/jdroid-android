package com.jdroid.android.share

class TwitterSharingItem(sharingData: SharingData) : AppSharingItem(sharingData) {

    override fun getSharingMedium(): SharingMedium {
        return SharingMedium.TWITTER
    }

    override fun getMinimumVersionCode(): Int? {
        return 420
    }
}
