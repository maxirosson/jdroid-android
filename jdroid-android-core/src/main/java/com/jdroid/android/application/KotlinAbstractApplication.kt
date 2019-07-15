package com.jdroid.android.application

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import com.google.android.play.core.splitcompat.SplitCompat
import com.jdroid.android.BuildConfig
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsFacade
import com.jdroid.android.firebase.testlab.FirebaseTestLab
import com.jdroid.android.leakcanary.LeakCanaryHelper
import com.jdroid.android.lifecycle.ApplicationLifecycleHelper
import com.jdroid.android.utils.ProcessUtils
import com.jdroid.java.utils.LoggerUtils
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.slf4j.Logger

abstract class KotlinAbstractApplication : Application() {

    companion object {
        /**
         * The LOGGER variable is initialized in the "OnCreate" method, after that "LoggerUtils" has been properly
         * configured by the superclass.
         */
        protected var LOGGER: Logger? = null
    }

    private fun initLogging() {
        if (LOGGER == null) {
            LoggerUtils.setEnabled(isLoggingEnabled())
            LOGGER = LoggerUtils.getLogger(AbstractApplication::class.java)
        }
    }

    protected open fun isLoggingEnabled(): Boolean {
        return isDebuggable() || isFirebaseTestLabLoggingEnabled() && FirebaseTestLab.isRunningInstrumentedTests
    }

    private fun isDebuggable(): Boolean {
        return this.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    protected open fun isFirebaseTestLabLoggingEnabled(): Boolean {
        return true
    }

    protected open fun isMultiProcessSupportEnabled(): Boolean {
        return BuildConfig.DEBUG && LeakCanaryHelper.isLeakCanaryEnabled()
    }

    @MainThread
    @CallSuper
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        if (isSplitCompatEnabled()) {
            SplitCompat.install(this)
        }

        onInitMultiDex()

        if (!isMultiProcessSupportEnabled() || ProcessUtils.isMainProcess(this)) {
            initLogging()
            ApplicationLifecycleHelper.attachBaseContext(this)
            onMainProcessAttachBaseContext()
        } else {
            onSecondaryProcessAttachBaseContext(ProcessUtils.getProcessInfo(this))
        }
    }

    protected open fun isSplitCompatEnabled(): Boolean {
        return false
    }

    @MainThread
    protected open fun onInitMultiDex() {
        // Do nothing
    }

    @MainThread
    protected open fun onMainProcessAttachBaseContext() {
        // Do nothing
    }

    @MainThread
    protected open fun onSecondaryProcessAttachBaseContext(processInfo: ActivityManager.RunningAppProcessInfo?) {
        // Do nothing
    }

    @MainThread
    open fun onProviderInit() {
        // Do nothing
    }

    protected open fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@KotlinAbstractApplication)

            // TODO See if its a good idea to let each lifecycle call back to initialize its own dependencies
            val coreModule = module {
                single { FirebaseAnalyticsFacade() }
            }
            loadKoinModules(arrayListOf(coreModule))
        }
    }

    @MainThread
    protected open fun onMainProcessCreate() {
        // Do nothing
    }

    @MainThread
    protected open fun onSecondaryProcessCreate(processInfo: ActivityManager.RunningAppProcessInfo?) {
        // Do nothing
    }

    @MainThread
    @CallSuper
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (!isMultiProcessSupportEnabled() || ProcessUtils.isMainProcess(this)) {
            ApplicationLifecycleHelper.onConfigurationChanged(this, newConfig)
            onMainProcessConfigurationChanged()
        } else {
            onSecondaryProcessConfigurationChanged(ProcessUtils.getProcessInfo(this))
        }
    }

    @MainThread
    protected open fun onMainProcessConfigurationChanged() {
        // Do nothing
    }

    @MainThread
    protected open fun onSecondaryProcessConfigurationChanged(processInfo: ActivityManager.RunningAppProcessInfo?) {
        // Do nothing
    }
}