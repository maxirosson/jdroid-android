package com.jdroid.android.sample.debug

import androidx.core.util.Pair

import com.jdroid.android.debug.DebugContext
import com.jdroid.android.sample.api.ApiServer
import com.jdroid.android.sample.firebase.remoteconfig.AndroidRemoteConfigParameter
import com.jdroid.java.collections.Lists
import com.jdroid.java.collections.Maps
import com.jdroid.java.http.Server
import com.jdroid.java.remoteconfig.RemoteConfigParameter

import java.util.Arrays

class AndroidDebugContext : DebugContext() {
    init {
        addCustomDebugInfoProperty(Pair("Sample Key", "Sample Value"))
    }

    override fun getServersMap(): Map<Class<out Server>, List<Server>> {
        val serversMap = Maps.newHashMap<Class<out Server>, List<Server>>()
        serversMap[ApiServer::class.java] = Lists.newArrayList(*ApiServer.values())
        return serversMap
    }

    override fun getUrlsToTest(): List<String> {
        val urls = Lists.newArrayList<String>()
        urls.add("http://jdroidtools.com")
        urls.add("http://jdroidtools.com/")
        urls.add("http://jdroidtools.com/uri")
        urls.add("http://jdroidtools.com/uri/singletop?a=1")
        urls.add("http://jdroidtools.com/uri/singletop?a=2")
        urls.add("http://jdroidtools.com/uri/singletop?a=3")
        urls.add("http://jdroidtools.com/uri/noflags?a=1")
        urls.add("http://jdroidtools.com/uri/noflags?a=2")
        urls.add("http://jdroidtools.com/uri/noflags?a=3")
        urls.add("http://jdroidtools.com/uri/noflags")
        urls.add("http://jdroidtools.com/uri/invalid")
        return urls
    }

    override fun getRemoteConfigParameters(): List<RemoteConfigParameter> {
        return Arrays.asList<RemoteConfigParameter>(*AndroidRemoteConfigParameter.values())
    }
}
