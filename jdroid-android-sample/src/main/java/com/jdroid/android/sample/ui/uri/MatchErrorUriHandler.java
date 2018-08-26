package com.jdroid.android.sample.ui.uri;

import android.net.Uri;

import com.jdroid.android.uri.AbstractUriHandler;

public class MatchErrorUriHandler extends AbstractUriHandler<MatchErrorActivity> {

	@Override
	public Boolean matches(Uri uri) {
		throw new RuntimeException();
	}
}
