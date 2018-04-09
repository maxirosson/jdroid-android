package com.jdroid.android.linkedin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class LinkedInHelper {
	
	public static void openCompanyPage(Context context, String companyPageId) {
		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company/" + companyPageId)));
	}
}
