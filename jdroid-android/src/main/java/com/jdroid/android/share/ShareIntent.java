package com.jdroid.android.share;

import android.app.Activity;
import android.content.Intent;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.java.http.MimeType;

/**
 * 
 * @author Maxi Rosson
 */
public class ShareIntent {
	
	public static void share(String appName, String shareEmailSubject, String shareEmailContent) {
		Activity activity = AbstractApplication.get().getCurrentActivity();
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType(MimeType.TEXT.toString());
		intent.putExtra(Intent.EXTRA_SUBJECT, shareEmailSubject);
		intent.putExtra(Intent.EXTRA_TEXT, shareEmailContent);
		activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.shareTitle, appName)));
	}
}
