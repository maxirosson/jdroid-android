package com.jdroid.android.linkedin;

import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.activity.ActivityLauncher;

public class LinkedInHelper {
	
	public static void openCompanyPage(String companyPageId) {
		ActivityLauncher.startActivityNewTask(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company/" + companyPageId)));
	}
}
