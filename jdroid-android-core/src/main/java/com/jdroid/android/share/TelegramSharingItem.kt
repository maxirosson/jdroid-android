package com.jdroid.android.share

class TelegramSharingItem(sharingData: SharingData) : AppSharingItem(sharingData) {

    override fun getSharingMedium(): SharingMedium {
        return SharingMedium.TELEGRAM
    }
}
