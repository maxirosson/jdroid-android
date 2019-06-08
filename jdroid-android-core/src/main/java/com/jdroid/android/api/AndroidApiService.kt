package com.jdroid.android.api

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.debug.http.HttpDebugConfiguration
import com.jdroid.android.http.HttpConfiguration
import com.jdroid.java.http.HttpServiceFactory
import com.jdroid.java.http.Server
import com.jdroid.java.http.api.AbstractApiService
import com.jdroid.java.http.cache.Cache
import com.jdroid.java.http.mock.AbstractMockHttpService

import java.io.File

abstract class AndroidApiService : AbstractApiService() {

    override fun getHttpCacheDirectory(cache: Cache?): File {
        return AbstractApplication.get().cacheManager!!.getFileSystemCacheDirectory(cache)
    }

    override fun isHttpMockEnabled(): Boolean? {
        return HttpDebugConfiguration.isHttpMockEnabled()
    }

    override fun getAbstractMockHttpServiceInstance(vararg urlSegments: Any): AbstractMockHttpService {
        return HttpDebugConfiguration.getAbstractMockHttpServiceInstance(*urlSegments)
    }

    override fun getServer(): Server {
        return AbstractApplication.get().appContext.server
    }

    override fun createHttpServiceFactory(): HttpServiceFactory {
        return HttpConfiguration.getHttpServiceFactory()
    }
}
