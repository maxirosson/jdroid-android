package com.jdroid.android.debug.facebook.stetho

import android.content.Context
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.context.BuildConfigUtils
import com.jdroid.android.http.HttpConfiguration
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback
import com.jdroid.java.http.HttpServiceFactory
import com.jdroid.java.utils.LoggerUtils
import com.jdroid.java.utils.ReflectionUtils

class StethoApplicationLifecycleCallback : ApplicationLifecycleCallback() {

    companion object {

        private val LOGGER = LoggerUtils.getLogger(StethoApplicationLifecycleCallback::class.java)

        private const val OKHTTP_SERVICE_FACTORY = "com.jdroid.java.http.okhttp.OkHttpServiceFactory"
        private const val OKHTTP_INTERCEPTOR = "okhttp3.Interceptor"
        private const val STETHO_OKHTTP_INTERCEPTOR = "com.facebook.stetho.okhttp3.StethoInterceptor"
        private const val ADD_NETWORK_INTERCEPTOR = "addNetworkInterceptor"
        private const val STETHO = "com.facebook.stetho.Stetho"
        private const val INITIALIZE_WITH_DEFAULTS = "initializeWithDefaults"
    }

    override fun onProviderInit(context: Context) {
        try {
            val okhttpServiceFactory = ReflectionUtils.safeNewInstance<HttpServiceFactory>(OKHTTP_SERVICE_FACTORY)
            if (okhttpServiceFactory != null) {
                val interceptorClass = ReflectionUtils.getClass(OKHTTP_INTERCEPTOR)
                val stethoInterceptor = ReflectionUtils.newInstance<Any>(STETHO_OKHTTP_INTERCEPTOR)
                ReflectionUtils.invokeMethod(
                    OKHTTP_SERVICE_FACTORY, okhttpServiceFactory, ADD_NETWORK_INTERCEPTOR,
                    listOf(interceptorClass), listOf(stethoInterceptor)
                )
                HttpConfiguration.httpServiceFactory = okhttpServiceFactory
                LOGGER.info("Stetho $STETHO_OKHTTP_INTERCEPTOR initialized")
            }
        } catch (e: Exception) {
            LOGGER.error("Error initializing $STETHO_OKHTTP_INTERCEPTOR", e)
        }
    }

    override fun onCreate(context: Context) {
        try {
            ReflectionUtils.invokeStaticMethod(
                STETHO, INITIALIZE_WITH_DEFAULTS,
                listOf<Class<*>>(Context::class.java), listOf<Any>(AbstractApplication.get())
            )
            LOGGER.info("Stetho initialized")
        } catch (e: Exception) {
            LOGGER.error("Error initializing Stetho", e)
        }
    }

    override fun isEnabled(): Boolean {
        return BuildConfigUtils.getBuildConfigBoolean("STETHO_ENABLED", false)!!
    }
}
