package com.jdroid.android.lifecycle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.jdroid.java.utils.LoggerUtils
import java.util.Locale

class LocaleChangedReceiver : BroadcastReceiver() {

    companion object {
        private val LOGGER = LoggerUtils.getLogger(LocaleChangedReceiver::class.java)
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null && Intent.ACTION_LOCALE_CHANGED == intent.action) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LOGGER.info("Changed display locale to " + Locale.getDefault(Locale.Category.DISPLAY))
                LOGGER.info("Changed format locale to " + Locale.getDefault(Locale.Category.FORMAT))
            } else {
                LOGGER.info("Changed locale to " + Locale.getDefault())
            }
            ApplicationLifecycleHelper.onLocaleChanged(context)
        }
    }
}
