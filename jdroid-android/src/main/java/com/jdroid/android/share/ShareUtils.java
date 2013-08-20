package com.jdroid.android.share;

import android.app.Activity;
import android.content.Intent;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.http.MimeType;

/**
 * 
 * @author Maxi Rosson
 */
public class ShareUtils {
	
	public static void shareTextContent(int shareTitle, int shareSubject, int shareText) {
		Activity activity = AbstractApplication.get().getCurrentActivity();
		shareTextContent(activity.getString(shareTitle), activity.getString(shareSubject),
			activity.getString(shareText));
	}
	
	public static void shareTextContent(String shareTitle, String shareSubject, String shareText) {
		Activity activity = AbstractApplication.get().getCurrentActivity();
		Intent intent = createShareTextContentIntent(shareSubject, shareText);
		activity.startActivity(Intent.createChooser(intent, shareTitle));
	}
	
	public static Intent createShareTextContentIntent(String shareSubject, String shareText) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType(MimeType.TEXT.toString());
		intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
		intent.putExtra(Intent.EXTRA_TEXT, shareText);
		return intent;
	}
}
