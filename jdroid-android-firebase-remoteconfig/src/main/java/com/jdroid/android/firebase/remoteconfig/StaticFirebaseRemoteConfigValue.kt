package com.jdroid.android.firebase.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.jdroid.java.remoteconfig.RemoteConfigParameter

// TODO See if defaultValue should be nullable or not
class StaticFirebaseRemoteConfigValue(private val remoteConfigParameter: RemoteConfigParameter) : FirebaseRemoteConfigValue {

    @Throws(IllegalArgumentException::class)
    override fun asLong(): Long {
        return remoteConfigParameter.getDefaultValue() as Long
    }

    @Throws(IllegalArgumentException::class)
    override fun asDouble(): Double {
        return remoteConfigParameter.getDefaultValue() as Double
    }

    override fun asString(): String {
        return remoteConfigParameter.getDefaultValue().toString()
    }

    override fun asByteArray(): ByteArray {
        return remoteConfigParameter.getDefaultValue() as ByteArray
    }

    @Throws(IllegalArgumentException::class)
    override fun asBoolean(): Boolean {
        return remoteConfigParameter.getDefaultValue() as Boolean
    }

    override fun getSource(): Int {
        return FirebaseRemoteConfig.VALUE_SOURCE_STATIC
    }
}
