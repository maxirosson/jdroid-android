package com.jdroid.android.sample.ui.uri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.jdroid.android.uri.AbstractUriHandler;

public class UriMapperSingleTopUriHandler extends AbstractUriHandler<UriMapperSingleTopActivity> {

	@Override
	public Boolean matches(Uri uri) {
		return uri.getQueryParameter("a").equals("1") || uri.getQueryParameter("a").equals("2");
	}

	@Override
	public Intent createMainIntent(@NonNull Activity activity, Uri uri) {
		return new Intent(activity, UriMapperSingleTopActivity.class);
	}

	@Override
	public String getUrl(UriMapperSingleTopActivity activity) {
		return "http://jdroidtools.com/uri/singletop?a=1";
	}
}
