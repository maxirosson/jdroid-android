package com.jdroid.android

import android.app.Activity

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.context.AppContext
import com.jdroid.android.context.BuildConfigUtils
import com.jdroid.android.debug.DebugContext
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback
import com.jdroid.android.lifecycle.ApplicationLifecycleHelper
import com.jdroid.java.utils.ReflectionUtils

import java.util.ArrayList

class TestAndroidApplication : AbstractApplication() {

    override fun onInitMultiDex() {
        // Multidex support doesn't play well with Robolectric yet

        BuildConfigUtils.setBuildConfigResolver(TestBuildConfigResolver())
        ReflectionUtils.setStaticField(
            ApplicationLifecycleHelper::class.java,
            "applicationLifecycleCallbacks",
            createApplicationLifecycleCallbacks()
        )
    }

    override fun initKoin() {
        // Do nothing
    }

    /**
     * This method can be overridden in subclasses to provide the list of ApplicationLifecycleCallback to use in the tests.
     *
     * @return a list of ApplicationLifecycleCallback to use in the tests, or empty list.
     */
    protected fun createApplicationLifecycleCallbacks(): List<ApplicationLifecycleCallback> {
        return ArrayList()
    }

    override fun isMultiProcessSupportEnabled(): Boolean? {
        return false
    }

    override fun getHomeActivityClass(): Class<out Activity>? {
        return null
    }

    override fun createAppContext(): AppContext {
        return TestAppContext()
    }

    override fun createDebugContext(): DebugContext {
        return TestDebugContext()
    }

    override fun getLauncherIconResId(): Int {
        return 0
    }

    override fun getNotificationIconResId(): Int {
        return 0
    }

    override fun getManifestPackageName(): String {
        return ""
    }

    override fun initExceptionHandlers() {
        Thread.setDefaultUncaughtExceptionHandler(TestExceptionHandler())
    }

    override fun verifyAppLaunchStatus() {
        // Do Nothing
    }
}
