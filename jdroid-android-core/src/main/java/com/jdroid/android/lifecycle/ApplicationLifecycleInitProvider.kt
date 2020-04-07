package com.jdroid.android.lifecycle

class ApplicationLifecycleInitProvider : AbstractInitProvider() {

    override fun init() {
        ApplicationLifecycleHelper.onProviderInit(context)
    }
}
