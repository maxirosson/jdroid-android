package com.jdroid.android.firebase.remoteconfig

import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.utils.SharedPreferencesHelper
import com.jdroid.java.remoteconfig.RemoteConfigLoader
import com.jdroid.java.remoteconfig.RemoteConfigParameter
import com.jdroid.java.utils.LoggerUtils
import com.jdroid.java.utils.StringUtils
import com.jdroid.java.utils.TypeUtils

class MockRemoteConfigLoader(fetch: Boolean) : RemoteConfigLoader {

    companion object {

        private val LOGGER = LoggerUtils.getLogger(MockRemoteConfigLoader::class.java)

        private const val MOCKS_ENABLED = "firebase.remote.config.mocks.enabled"

        private var mocksEnabled: Boolean? = null

        fun get(): MockRemoteConfigLoader {
            return AbstractApplication.get().remoteConfigLoader as MockRemoteConfigLoader
        }

        @RestrictTo(LIBRARY)
        fun setMocksEnabled(mocksEnabled: Boolean) {
            this.mocksEnabled = mocksEnabled
            AbstractApplication.get().remoteConfigLoader = if (mocksEnabled) MockRemoteConfigLoader(false) else FirebaseRemoteConfigLoader()
            SharedPreferencesHelper.get(MockRemoteConfigLoader::class.java).savePreference(MOCKS_ENABLED, mocksEnabled)
        }

        @RestrictTo(LIBRARY)
        fun isMocksEnabled(): Boolean {
            if (mocksEnabled == null) {
                mocksEnabled = SharedPreferencesHelper.get(MockRemoteConfigLoader::class.java)
                    .loadPreferenceAsBoolean(MOCKS_ENABLED, false)
            }
            return this.mocksEnabled!!
        }
    }

    private val sharedPreferencesHelper: SharedPreferencesHelper = SharedPreferencesHelper.get(MockRemoteConfigLoader::class.java)
    private val cache: MutableMap<String, String>

    init {
        cache = if (fetch) sharedPreferencesHelper.loadAllPreferences() as MutableMap<String, String> else hashMapOf()
    }

    override fun fetch() {
        // Do nothing
    }

    override fun getObject(remoteConfigParameter: RemoteConfigParameter): Any? {
        return getValue(remoteConfigParameter, cache[remoteConfigParameter.getKey()])
    }

    override fun getString(remoteConfigParameter: RemoteConfigParameter): String? {
        val value = getValue(remoteConfigParameter, cache[remoteConfigParameter.getKey()])
        return value?.toString()
    }

    override fun getStringList(remoteConfigParameter: RemoteConfigParameter): List<String>? {
        return getValue(remoteConfigParameter, StringUtils.splitWithCommaSeparator(getString(remoteConfigParameter))) as List<String>?
    }

    override fun getBoolean(remoteConfigParameter: RemoteConfigParameter): Boolean? {
        return getValue(remoteConfigParameter, TypeUtils.getBoolean(cache[remoteConfigParameter.getKey()])) as Boolean?
    }

    override fun getLong(remoteConfigParameter: RemoteConfigParameter): Long? {
        return getValue(remoteConfigParameter, TypeUtils.getLong(cache[remoteConfigParameter.getKey()])) as Long?
    }

    override fun getDouble(remoteConfigParameter: RemoteConfigParameter): Double? {
        return getValue(remoteConfigParameter, TypeUtils.getDouble(cache[remoteConfigParameter.getKey()])) as Double?
    }

    private operator fun getValue(remoteConfigParameter: RemoteConfigParameter, value: Any?): Any? {
        var theValue = value
        if (theValue == null) {
            theValue = remoteConfigParameter.getDefaultValue()
        }
        if (theValue is Int) {
            theValue = theValue.toLong()
        }
        if (LoggerUtils.isEnabled()) {
            LOGGER.info("Loaded Mock Remote Config. Key [" + remoteConfigParameter.getKey() + "] Value [" + theValue + "]")
        }
        return theValue
    }

    @RestrictTo(LIBRARY)
    fun saveRemoteConfigParameter(remoteConfigParameter: RemoteConfigParameter, value: String) {
        sharedPreferencesHelper.savePreferenceAsync(remoteConfigParameter.getKey(), value)
        cache[remoteConfigParameter.getKey()] = value
    }
}
