package com.jdroid.android.sample.ui.uri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.jdroid.android.uri.AbstractUriHandler;

public class MainIntentErrorUriHandler extends AbstractUriHandler<MainIntentErrorActivity> {
	@Override
	public Boolean matches(Uri uri) {
		return true;
	}

	@Override
	public Intent createMainIntent(@NonNull Activity activity, Uri uri) {
		throw new RuntimeException();
	}
}
