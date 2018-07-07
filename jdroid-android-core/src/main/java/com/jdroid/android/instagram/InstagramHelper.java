package com.jdroid.android.instagram;

import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.activity.ActivityLauncher;

public class InstagramHelper {
	
	public static void openProfile(String account) {
		ActivityLauncher.startActivityNewTask(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/" + account)));
	}
	
}
