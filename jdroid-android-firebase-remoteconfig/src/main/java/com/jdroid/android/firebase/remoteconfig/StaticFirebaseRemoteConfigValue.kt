package com.jdroid.android.firebase.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.jdroid.java.remoteconfig.RemoteConfigParameter

class StaticFirebaseRemoteConfigValue(private val remoteConfigParameter: RemoteConfigParameter) : FirebaseRemoteConfigValue {

    @Throws(IllegalArgumentException::class)
    override fun asLong(): Long {
        return remoteConfigParameter.defaultValue as Long
    }

    @Throws(IllegalArgumentException::class)
    override fun asDouble(): Double {
        return remoteConfigParameter.defaultValue as Double
    }

    override fun asString(): String {
        return remoteConfigParameter.defaultValue.toString()
    }

    override fun asByteArray(): ByteArray {
        return remoteConfigParameter.defaultValue as ByteArray
    }

    @Throws(IllegalArgumentException::class)
    override fun asBoolean(): Boolean {
        return remoteConfigParameter.defaultValue as Boolean
    }

    override fun getSource(): Int {
        return FirebaseRemoteConfig.VALUE_SOURCE_STATIC
    }
}
