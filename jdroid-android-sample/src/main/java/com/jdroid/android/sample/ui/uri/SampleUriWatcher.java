package com.jdroid.android.sample.ui.uri;

import android.net.Uri;
import androidx.annotation.NonNull;

import com.jdroid.android.uri.UriWatcher;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class SampleUriWatcher implements UriWatcher {

	private static final Logger LOGGER = LoggerUtils.getLogger(SampleUriWatcher.class);

	@Override
	public void onUriOpened(@NonNull Uri uri) {
		LOGGER.debug("SampleUriWatcher executed for Uri " + uri.toString());
	}
}
