package com.jdroid.android.firebase.remoteconfig

import android.content.Context
import android.os.Bundle

import com.jdroid.android.firebase.jobdispatcher.ServiceCommand

class FirebaseRemoteConfigFetchCommand : ServiceCommand() {

    companion object {
        private const val SET_EXPERIMENT_USER_PROPERTY = "setExperimentUserProperty"
    }

    override fun start() {
        start(false)
    }

    fun start(setExperimentUserProperty: Boolean?) {
        val bundle = Bundle()
        bundle.putBoolean(FirebaseRemoteConfigFetchCommand.SET_EXPERIMENT_USER_PROPERTY, setExperimentUserProperty!!)
        start(bundle)
    }

    override fun execute(context: Context, bundle: Bundle): Boolean {
        val setExperimentUserProperty = bundle.getBoolean(SET_EXPERIMENT_USER_PROPERTY)
        FirebaseRemoteConfigLoader.get().fetch(setExperimentUserProperty, null)
        return false
    }
}
