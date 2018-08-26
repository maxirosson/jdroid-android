package com.jdroid.android.sample.ui.uri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.jdroid.android.uri.AbstractUriHandler;

public class DefaultIntentErrorUriHandler extends AbstractUriHandler<DefaulItntentErrorActivity> {

	@Override
	public Boolean matches(Uri uri) {
		return false;
	}

	@Override
	public Intent createDefaultIntent(@NonNull Activity activity, Uri uri) {
		throw new RuntimeException();
	}
}
