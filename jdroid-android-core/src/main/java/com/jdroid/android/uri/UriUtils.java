package com.jdroid.android.uri;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.intent.IntentUtils;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.widget.WidgetHelper;
import com.jdroid.java.utils.RandomUtils;
import com.jdroid.java.utils.StringUtils;

import static com.jdroid.android.notification.NotificationBuilder.NOTIFICATION_SCHEME;

public class UriUtils {

	private static final String ORIGINAL_URI = "originalUri";
	private static final String RANDOM_PARAMETER = "rnd";
	private static final String INTERNAL_SCHEME = "internal";

	public static Intent createInternalIntent(@NonNull Context context, String url) {
		return createIntent(context, url, INTERNAL_SCHEME + "://" + AppUtils.getApplicationId());
	}

	public static Intent createIntent(@NonNull Context context, String url, @NonNull String referrer) {
		Intent intent;
		if (url != null) {
			intent = new Intent();
			intent.putExtra(ORIGINAL_URI, url);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(UriUtils.addRandomParam(Uri.parse(url)));
			intent.setPackage(AppUtils.getApplicationId());
			if (!IntentUtils.INSTANCE.isIntentAvailable(intent)) {
				intent = new Intent(context, AbstractApplication.get().getHomeActivityClass());
				AbstractApplication.get().getExceptionHandler().logHandledException("Url is not valid: " + url);
			}
		} else {
			intent = new Intent(context, AbstractApplication.get().getHomeActivityClass());
			AbstractApplication.get().getExceptionHandler().logHandledException("Missing url to create intent");
		}
		ReferrerUtils.setReferrer(intent, referrer);
		return intent;
	}

	public static Uri addRandomParam(Uri uri) {
		String uriString = uri.toString();
		StringBuilder builder = new StringBuilder(uriString);
		if (uri.getPathSegments().isEmpty() && StringUtils.isEmpty(uri.getQuery()) && !uriString.endsWith("/")) {
			builder.append("/");
		}
		if (StringUtils.isEmpty(uri.getQuery())) {
			builder.append("?");
		} else {
			builder.append("&");
		}
		builder.append(RANDOM_PARAMETER);
		builder.append("=");
		builder.append(RandomUtils.getInt());
		return Uri.parse(builder.toString());
	}


	public static Uri getUri(Intent intent) {
		Uri uri = intent.getData();
		if (uri != null) {
			String originalUri = intent.getStringExtra(ORIGINAL_URI);
			if (originalUri != null) {
				uri = Uri.parse(originalUri);
			}
		}
		return uri != null && (uri.getScheme().equals(NOTIFICATION_SCHEME) || uri.getScheme().equals(WidgetHelper.WIDGET_SCHEME)) ? null : uri;
	}

	public static Uri getUri(Activity activity) {
		return getUri(activity.getIntent());
	}

	public static Boolean isInternalReferrerCategory(String referrerCategory) {
		return referrerCategory.startsWith(INTERNAL_SCHEME + "://");
	}
}
