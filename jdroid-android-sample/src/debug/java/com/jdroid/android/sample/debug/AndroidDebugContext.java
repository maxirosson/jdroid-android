package com.jdroid.android.sample.debug;

import android.support.v4.util.Pair;

import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.sample.api.ApiServer;
import com.jdroid.android.sample.firebase.remoteconfig.AndroidRemoteConfigParameter;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.http.Server;
import com.jdroid.java.remoteconfig.RemoteConfigParameter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AndroidDebugContext extends DebugContext {

	public AndroidDebugContext() {
		addCustomDebugInfoProperty(new Pair<String, Object>("Sample Key", "Sample Value"));
	}

	@Override
	public Map<Class<? extends Server>, List<? extends Server>> getServersMap() {
		Map<Class<? extends Server>, List<? extends Server>> serversMap = Maps.newHashMap();
		serversMap.put(ApiServer.class, Lists.newArrayList(ApiServer.values()));
		return serversMap;
	}

	@Override
	public List<String> getUrlsToTest() {
		List<String> urls = Lists.newArrayList();
		urls.add("http://jdroidtools.com");
		urls.add("http://jdroidtools.com/");
		urls.add("http://jdroidtools.com/uri");
		urls.add("http://jdroidtools.com/uri/singletop?a=1");
		urls.add("http://jdroidtools.com/uri/singletop?a=2");
		urls.add("http://jdroidtools.com/uri/singletop?a=3");
		urls.add("http://jdroidtools.com/uri/noflags?a=1");
		urls.add("http://jdroidtools.com/uri/noflags?a=2");
		urls.add("http://jdroidtools.com/uri/noflags?a=3");
		urls.add("http://jdroidtools.com/uri/noflags");
		urls.add("http://jdroidtools.com/uri/invalid");
		return urls;
	}
	
	@Override
	public List<RemoteConfigParameter> getRemoteConfigParameters() {
		return Arrays.asList(AndroidRemoteConfigParameter.values());
	}
}
