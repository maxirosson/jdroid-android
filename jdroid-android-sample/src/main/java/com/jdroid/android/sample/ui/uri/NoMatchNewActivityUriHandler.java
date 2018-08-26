package com.jdroid.android.sample.ui.uri;

import android.net.Uri;

import com.jdroid.android.uri.AbstractUriHandler;

public class NoMatchNewActivityUriHandler extends AbstractUriHandler<NoMatchNewActivity> {

	@Override
	public Boolean matches(Uri uri) {
		return false;
	}
}
