package com.jdroid.android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jdroid.java.utils.LoggerUtils

class InstallReferrerReceiver(private val receivers: List<BroadcastReceiver>) : BroadcastReceiver() {

    companion object {
        private val LOGGER = LoggerUtils.getLogger(InstallReferrerReceiver::class.java)
        private const val INSTALL_ACTION = "com.android.vending.INSTALL_REFERRER"
    }

    override fun onReceive(context: Context, intent: Intent) {
        try {
            if (INSTALL_ACTION == intent.action) {
                for (each in receivers) {
                    try {
                        LOGGER.warn("Executing " + each.javaClass.simpleName)
                        each.onReceive(context, intent)
                    } catch (e: Exception) {
                        LOGGER.error("Error when executing " + each.javaClass.simpleName, e)
                    }
                }
            }
        } catch (e: Exception) {
            LOGGER.error("Error when executing receivers", e)
        }
    }
}
