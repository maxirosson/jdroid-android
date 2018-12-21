package com.jdroid.android.uri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.google.firebase.appindexing.Action;

import androidx.annotation.NonNull;

/**
 * Handler which parse the parameters and create an intent based on an uri
 */
public interface UriHandler<T extends Activity> {

	/**
	 * @return Whether the uri should be transformed to a main or a default intent
	 */
	public Boolean matches(@NonNull Uri uri);

	/**
	 * Create the intent for the proper activity based on the uri.
	 *
	 * @param activity the {@link Activity}.
	 * @param uri the uri to handle.
	 * @return the Intent.
	 */
	public Intent createMainIntent(@NonNull Activity activity, @NonNull Uri uri);

	public Intent createDefaultIntent(@NonNull Activity activity, @NonNull Uri uri);

	public void logUriNotMatch(@NonNull Uri uri);

	public Boolean isAppIndexingEnabled(T activity);

	public Action getAppIndexingAction(T activity);

	public String getUrl(T activity);
}
