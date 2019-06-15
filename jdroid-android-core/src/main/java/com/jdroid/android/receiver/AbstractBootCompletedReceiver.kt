package com.jdroid.android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.MainThread

import com.jdroid.android.application.AbstractApplication

abstract class AbstractBootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
            try {
                onBootCompleted()
            } catch (e: Exception) {
                AbstractApplication.get().exceptionHandler.logHandledException(e)
            }
        }
    }

    @MainThread
    protected abstract fun onBootCompleted()
}
