package com.jdroid.android.share

class WhatsAppSharingItem(sharingData: SharingData) : AppSharingItem(sharingData) {

    override fun getSharingMedium(): SharingMedium {
        return SharingMedium.WHATSAPP
    }

    override fun getMinimumVersionCode(): Int? {
        return 49000
    }
}
