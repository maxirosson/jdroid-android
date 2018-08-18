package com.jdroid.android.sample.ui.uri;

import android.net.Uri;

import com.jdroid.android.uri.AbstractUriHandler;

public class MatchNullIntentActivityUriHandler extends AbstractUriHandler<MatchNullIntentActivity> {

	@Override
	public Boolean matches(Uri uri) {
		return true;
	}
}
