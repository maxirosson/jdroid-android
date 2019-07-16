package com.jdroid.android.sample.ui.about

import com.jdroid.android.about.SpreadTheLoveFragment
import com.jdroid.android.sample.R

class AndroidSpreadTheLoveFragment : SpreadTheLoveFragment() {

    companion object {
        const val TWITTER_SHARE_URL = "http://goo.gl/XcxvIh"
        const val GOOGLE_PLUS_SHARE_URL = "http://goo.gl/6BloFX"
        const val FACEBOOK_SHARE_URL = "http://goo.gl/ogJoNX"
        const val WHATSAPP_SHARE_URL = "http://goo.gl/6KGhXn"
        const val TELEGRAM_SHARE_URL = "http://goo.gl/P4t4v0"
        const val HANGOUTS_SHARE_URL = "http://goo.gl/vQfk3U"
        const val SMS_SHARE_URL = "http://goo.gl/2rPlB3"
        const val UNKNOWN_SHARE_URL = "http://goo.gl/6kex8n"
    }

    override fun displayAppInviteButton(): Boolean {
        return true
    }

    override fun getDefaultShareText(): String {
        return getString(R.string.shareMessage, UNKNOWN_SHARE_URL)
    }

    override fun getTwitterShareText(): String {
        return getString(R.string.shareMessage, TWITTER_SHARE_URL)
    }

    override fun getSmsShareText(): String {
        return getString(R.string.shareMessage, SMS_SHARE_URL)
    }

    override fun getWhatsAppShareText(): String {
        return getString(R.string.shareMessage, WHATSAPP_SHARE_URL)
    }

    override fun getHangoutsShareText(): String {
        return getString(R.string.shareMessage, HANGOUTS_SHARE_URL)
    }

    override fun getTelegramShareText(): String {
        return getString(R.string.shareMessage, TELEGRAM_SHARE_URL)
    }
}
