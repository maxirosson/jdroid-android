package com.jdroid.android.application

import android.content.Context

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback

class AppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onProviderInit(context: Context) {
        AbstractApplication.get().onProviderInit()
    }

    override fun onLocaleChanged(context: Context) {
        AbstractApplication.get().onLocaleChanged()
    }

    override fun getInitOrder(): Int {
        return Integer.MAX_VALUE
    }
}
