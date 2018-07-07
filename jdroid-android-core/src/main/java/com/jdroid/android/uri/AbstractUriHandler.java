package com.jdroid.android.uri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.firebase.appindexing.Action;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public abstract class AbstractUriHandler<T extends Activity> implements UriHandler<T> {

	private final static Logger LOGGER = LoggerUtils.getLogger(AbstractUriHandler.class);

	@Override
	public Boolean matches(Uri uri) {
		return true;
	}

	@Override
	public void logUriNotMatch(Uri uri) {
		AbstractApplication.get().getExceptionHandler().logWarningException(getClass().getSimpleName() + " matches the default intent: " + uri.toString());
	}

	@Override
	public Intent createMainIntent(@NonNull Activity activity, Uri uri) {
		return null;
	}

	@Override
	public Intent createDefaultIntent(@NonNull Activity activity, Uri uri) {
		return new Intent(activity, AbstractApplication.get().getHomeActivityClass());
	}

	@Override
	public String getUrl(T activity) {
		return null;
	}

	@Override
	public Boolean isAppIndexingEnabled(T activity) {
		return true;
	}

	@Override
	public Action getAppIndexingAction(T activity) {
		if (isAppIndexingEnabled(activity)) {
			String url = getUrl(activity);
			if (url != null) {
				String actionType = Action.Builder.VIEW_ACTION;
				String title = getAppIndexingTitle(activity);
				LOGGER.debug("New App Indexing Action created. Type: " + actionType + " | Title: " + title + " | Url: " + url);
				return new Action.Builder(actionType).setObject(title, url).build();
			}
		}
		return null;
	}

	protected String getAppIndexingTitle(T activity) {
		return activity.getTitle().toString();
	}
}
