package com.jdroid.android.sample.ui.uri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.uri.AbstractUriHandler;

public class MatchNewActivityUriHandler extends AbstractUriHandler<MatchNewActivity> {
	@Override
	public Boolean matches(Uri uri) {
		return true;
	}

	@Override
	public Intent createMainIntent(@NonNull Activity activity, Uri uri) {
		return new Intent(activity, AbstractApplication.get().getHomeActivityClass());
	}
}
