package com.jdroid.android.instagram;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class InstagramHelper {
	
	public static void openProfile(Context context, String account) {
		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/" + account)));
	}
	
}
