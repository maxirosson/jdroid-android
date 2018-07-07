package com.jdroid.android.google;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.jdroid.android.R;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.dialog.AlertDialogFragment;
import com.jdroid.android.intent.IntentUtils;
import com.jdroid.android.utils.AppUtils;

public class GooglePlayUtils {
	
	private static final String GOOGLE_PLAY_DETAILS_LINK = "http://play.google.com/store/apps/details?id=";
	
	public static class UpdateAppDialogFragment extends AlertDialogFragment {
		
		@Override
		protected void onPositiveClick() {
			launchAppDetails(AppUtils.getReleaseApplicationId());
			getActivity().finish();
		}
	}
	
	public static class DownloadAppDialogFragment extends AlertDialogFragment {
		
		private static final String PACKAGE_NAME = "PACKAGE_NAME";
		
		@Override
		protected void onPositiveClick() {
			launchAppDetails(getArguments().getString(PACKAGE_NAME));
		}
		
		public void setPackageName(String packageName) {
			addParameter(PACKAGE_NAME, packageName);
		}
	}
	
	public static void showUpdateDialog(@Nullable FragmentActivity fragmentActivity) {
		if (fragmentActivity != null) {
			String title = fragmentActivity.getString(R.string.jdroid_updateAppTitle);
			String message = fragmentActivity.getString(R.string.jdroid_updateAppMessage);
			AlertDialogFragment.show(fragmentActivity, new UpdateAppDialogFragment(), title, message, null, null,
					fragmentActivity.getString(R.string.jdroid_ok), false);
		}
	}
	
	public static void showDownloadDialog(@Nullable FragmentActivity fragmentActivity, int appNameResId, String packageName) {
		if (fragmentActivity != null) {
			String appName = fragmentActivity.getString(appNameResId);
			String title = fragmentActivity.getString(R.string.jdroid_installAppTitle, appName);
			String message = fragmentActivity.getString(R.string.jdroid_installAppMessage, appName);
			DownloadAppDialogFragment fragment = new DownloadAppDialogFragment();
			fragment.setPackageName(packageName);
			AlertDialogFragment.show(fragmentActivity, fragment, title, message, fragmentActivity.getString(R.string.jdroid_no),
					null, fragmentActivity.getString(R.string.jdroid_yes), true);
		}
	}
	
	public static void launchAppDetails() {
		launchAppDetails(AppUtils.getReleaseApplicationId());
	}
	
	public static void launchAppDetails(String packageName) {
		Uri uri = Uri.parse("market://details?id=" + packageName);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		if (IntentUtils.isIntentAvailable(intent)) {
			ActivityLauncher.startActivityNewTask(intent);
		} else {
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(getGooglePlayLink(packageName)));
			ActivityLauncher.startActivityNewTask(intent);
		}
	}
	
	public static String getGooglePlayLink(String packageName, String referrer) {
		StringBuilder builder = new StringBuilder();
		builder.append(GOOGLE_PLAY_DETAILS_LINK);
		builder.append(packageName);
		if (referrer != null) {
			builder.append("&referrer=");
			builder.append(referrer);
		}
		return builder.toString();
	}
	
	public static String getGooglePlayLink(String packageName) {
		return getGooglePlayLink(packageName, null);
	}
	
	public static String getGooglePlayLink() {
		return getGooglePlayLink(AppUtils.getReleaseApplicationId());
	}
}
