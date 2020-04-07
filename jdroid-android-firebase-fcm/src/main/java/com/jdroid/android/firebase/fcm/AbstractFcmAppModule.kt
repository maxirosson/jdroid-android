package com.jdroid.android.firebase.fcm

import android.os.Bundle

import com.jdroid.android.activity.AbstractFragmentActivity
import com.jdroid.android.activity.ActivityDelegate
import com.jdroid.android.application.AbstractAppModule
import com.jdroid.android.application.AbstractApplication

abstract class AbstractFcmAppModule : AbstractAppModule() {

    val fcmMessageResolver: FcmMessageResolver by lazy { createFcmMessageResolver() }
    val fcmListenerResolver: FcmListenerResolver by lazy { createFcmListenerResolver() }
    private var fcmInitialized: Boolean = false

    abstract fun getFcmSenders(): List<FcmSender>

    protected fun getSubscriptionTopics(): List<String>? {
        return null
    }

    open fun createFcmMessageResolver(): FcmMessageResolver {
        return DefaultFcmMessageResolver()
    }

    open fun createFcmListenerResolver(): FcmListenerResolver {
        return FcmListenerResolver()
    }

    // TODO This FCM registration is not necessary
    override fun createActivityDelegate(abstractFragmentActivity: AbstractFragmentActivity): ActivityDelegate? {
        return if (!fcmInitialized)
            object : ActivityDelegate(abstractFragmentActivity) {

                override fun onCreate(savedInstanceState: Bundle?) {
                    if (!fcmInitialized) {
                        startFcmRegistration(true)
                        fcmInitialized = true
                    }
                }
            }
        else {
            null
        }
    }

    override fun onGooglePlayServicesUpdated() {
        startFcmRegistration(false)
    }

    fun startFcmRegistration(updateLastActiveTimestamp: Boolean) {
        FcmRegistrationWorker.start(updateLastActiveTimestamp);
    }

    companion object {

        val MODULE_NAME: String = AbstractFcmAppModule::class.java.name

        fun get(): AbstractFcmAppModule {
            return AbstractApplication.get().getAppModule(MODULE_NAME) as AbstractFcmAppModule
        }
    }
}
