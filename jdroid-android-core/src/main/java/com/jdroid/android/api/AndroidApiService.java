package com.jdroid.android.api;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.http.HttpDebugConfiguration;
import com.jdroid.android.http.HttpConfiguration;
import com.jdroid.java.http.HttpServiceFactory;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.api.AbstractApiService;
import com.jdroid.java.http.cache.Cache;
import com.jdroid.java.http.mock.AbstractMockHttpService;

import java.io.File;

public abstract class AndroidApiService extends AbstractApiService {

	@Override
	protected File getHttpCacheDirectory(Cache cache) {
		return AbstractApplication.get().getCacheManager().getFileSystemCacheDirectory(cache);
	}

	@Override
	protected Boolean isHttpMockEnabled() {
		return HttpDebugConfiguration.isHttpMockEnabled();
	}

	@Override
	protected AbstractMockHttpService getAbstractMockHttpServiceInstance(Object... urlSegments) {
		return HttpDebugConfiguration.getAbstractMockHttpServiceInstance(urlSegments);
	}

	@Override
	protected Server getServer() {
		return AbstractApplication.get().getAppContext().getServer();
	}

	@Override
	protected HttpServiceFactory createHttpServiceFactory() {
		return HttpConfiguration.getHttpServiceFactory();
	}
}
