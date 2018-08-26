package com.jdroid.android.uri;

import android.net.Uri;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

public interface UriWatcher {

	/**
	 * Called when an Uri is opened on the App
	 * <p>
	 * This method shouldn't perform long operations, because could block the user interface.
	 *
	 * @param uri The opened Uri
	 */
	@MainThread
	public void onUriOpened(@NonNull Uri uri);

}
