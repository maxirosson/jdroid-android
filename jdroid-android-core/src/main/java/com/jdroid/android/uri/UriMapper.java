package com.jdroid.android.uri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

import static android.support.annotation.RestrictTo.Scope.LIBRARY;

/**
 * Mapper which allows to navigate the application using a Uri.
 */
public class UriMapper {

	private static final Logger LOGGER = LoggerUtils.getLogger(UriMapper.class);

	private List<UriWatcher> uriWatchers = Lists.newArrayList();

	@RestrictTo(LIBRARY)
	public Boolean handleUri(@NonNull Activity activity, Intent intent, @Nullable UriHandler uriHandler, Boolean onActivityCreation) {
		Uri uri = UriUtils.getUri(intent);
		if (uri != null) {
			notifyToUriWatchers(uri);
			if (uriHandler != null) {
				String referrerCategory = ReferrerUtils.getReferrerCategory(activity);
				try {
					if (uriHandler.matches(uri)) {
						LOGGER.debug(uriHandler.getClass().getSimpleName() + " matches the main intent: " + uri.toString());
						Intent mainIntent = uriHandler.createMainIntent(activity, uri);
						handleIntent(mainIntent, activity, uri, referrerCategory, onActivityCreation);
					} else {
						uriHandler.logUriNotMatch(uri);
						handleDefaultIntent(activity, uriHandler, uri, referrerCategory, onActivityCreation);
					}
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(new UnexpectedException("Error matching: " + uri.toString(), e));
					try {
						handleDefaultIntent(activity, uriHandler, uri, referrerCategory, onActivityCreation);
					} catch (Exception e2) {
						Intent homeIntent = new Intent(activity, AbstractApplication.get().getHomeActivityClass());
						activity.finish();
						activity.startActivity(homeIntent);
					}
				}
				return true;
			} else {
				AbstractApplication.get().getExceptionHandler().logHandledException(new UnexpectedException("No uriHandler defined for: " + uri.toString()));
			}
		}
		return false;
	}

	private void handleIntent(Intent intent, Activity activity, Uri uri, String referrerCategory, Boolean onActivityCreation) {
		if (intent != null) {
			ReferrerUtils.setReferrer(intent, referrerCategory);
			String className = intent.getComponent().getShortClassName();
			int dot = className.lastIndexOf('.');
			if (dot != -1) {
				className = className.substring(dot + 1);
			}
			if (!UriUtils.isInternalReferrerCategory(referrerCategory)) {
				AbstractApplication.get().getCoreAnalyticsSender().trackUriOpened(className, uri, referrerCategory);
			}
			if (activity.getIntent().getComponent().equals(intent.getComponent())) {
				intent.setData(uri);
				activity.setIntent(intent);
			} else {
				if (onActivityCreation) {
					activity.finish();
				}
				activity.startActivity(intent);
			}
		} else {
			if (!UriUtils.isInternalReferrerCategory(referrerCategory)) {
				AbstractApplication.get().getCoreAnalyticsSender().trackUriOpened(activity.getClass().getSimpleName(), uri, referrerCategory);
			}
		}
	}

	private void handleDefaultIntent(@NonNull Activity activity, UriHandler uriHandler, Uri uri, String referrerCategory, Boolean onActivityCreation) {
		Intent intent = uriHandler.createDefaultIntent(activity, uri);
		handleIntent(intent, activity, uri, referrerCategory, onActivityCreation);
	}

	private void notifyToUriWatchers(final Uri uri) {
		if (!Lists.isNullOrEmpty(uriWatchers)) {
			for (UriWatcher each : uriWatchers) {
				LOGGER.debug("Notifying opened Uri to " + each.getClass().getSimpleName());
				try {
					each.onUriOpened(uri);
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
				}
			}
		}
	}

	public void addUriWatcher(@NonNull UriWatcher uriWatcher) {
		this.uriWatchers.add(uriWatcher);
	}
}