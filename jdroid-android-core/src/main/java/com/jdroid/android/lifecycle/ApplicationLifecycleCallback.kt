package com.jdroid.android.lifecycle

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.MainThread

/**
 * Callback for monitoring application lifecycle events. These callbacks are invoked on the main
 * thread, so any long operations or violating the strict mode policies should be avoided.
 */
abstract class ApplicationLifecycleCallback : Comparable<ApplicationLifecycleCallback> {

    @MainThread
    open fun attachBaseContext(base: Context) {
        // Do nothing
    }

    open fun isAttachBaseContextCallbackEnabled(): Boolean {
        return false
    }

    @MainThread
    open fun onProviderInit(context: Context) {
        // Do nothing
    }

    @MainThread
    open fun onCreate(context: Context) {
        // Do nothing
    }

    @MainThread
    open fun onConfigurationChanged(context: Context, newConfig: Configuration) {
        // Do nothing
    }

    @MainThread
    open fun onLowMemory(context: Context) {
        // Do nothing
    }

    @MainThread
    open fun onLocaleChanged(context: Context) {
        // Do nothing
    }

    /*
	 * The order in which the application listener should be invoked, relative to other application listeners.
	 * When there are dependencies among callbacks, setting this attribute for each of them ensures that they are created in the order required by those dependencies.
	 * The value is a simple integer, with higher numbers being invoked first. Zero is the default value and negative numbers are accepted.
	 */
    open fun getInitOrder(): Int {
        return 0
    }

    override fun compareTo(other: ApplicationLifecycleCallback): Int {
        return other.getInitOrder().compareTo(getInitOrder())
    }

    open fun isEnabled(): Boolean {
        return true
    }
}
