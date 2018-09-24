package com.jdroid.android.sample.ui.uri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.jdroid.android.sample.ui.home.HomeActivity;
import com.jdroid.android.uri.AbstractUriHandler;

public class UriMapperNoFlagsUriHandler extends AbstractUriHandler<UriMapperNoFlagsActivity> {

	@Override
	public Boolean matches(Uri uri) {
		return uri.getQueryParameter("a").equals("1") || uri.getQueryParameter("a").equals("2");
	}

	@Override
	public Intent createDefaultIntent(@NonNull Activity activity, Uri uri) {
		return new Intent(activity, HomeActivity.class);
	}

	@Override
	public String getUrl(UriMapperNoFlagsActivity activity) {
		return "http://jdroidtools.com/uri/noflags?a=1";
	}
}
