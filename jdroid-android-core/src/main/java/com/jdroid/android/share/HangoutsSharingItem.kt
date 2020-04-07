package com.jdroid.android.share

class HangoutsSharingItem(sharingData: SharingData) : AppSharingItem(sharingData) {

    override fun getSharingMedium(): SharingMedium {
        return SharingMedium.HANGOUTS
    }

    override fun getMinimumVersionCode(): Int? {
        return 23370799
    }
}
