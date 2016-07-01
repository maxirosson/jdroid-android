package com.jdroid.android.activity;

import java.io.Serializable;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.jdroid.android.application.AbstractApplication;

/**
 * Launcher for all the activities of the application
 */
public class ActivityLauncher {

	private ActivityLauncher(){}
	
	/**
	 * Launches the {@link AbstractApplication#getHomeActivityClass()}
	 */
	public static void launchHomeActivity() {
		Activity currentActivity = AbstractApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != AbstractApplication.get().getHomeActivityClass()) {
			Intent intent = new Intent(currentActivity, AbstractApplication.get().getHomeActivityClass());
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			currentActivity.startActivity(intent);
		}
	}
	
	public static void launchActivityClearTask(Class<? extends Activity> targetActivityClass,
			Boolean requiresAuthentication) {
		Activity currentActivity = AbstractApplication.get().getCurrentActivity();
		Intent intent = new Intent(AbstractApplication.get(), targetActivityClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		currentActivity.startActivity(intent);
	}
	
	/**
	 * Launches a new {@link Activity}
	 * 
	 * @param targetActivityClass The target {@link Activity} class to launch
	 */
	public static void launchActivity(Class<? extends Activity> targetActivityClass) {
		Activity currentActivity = AbstractApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != targetActivityClass) {
			Intent intent = new Intent(currentActivity, targetActivityClass);
			currentActivity.startActivity(intent);
		}
	}
	
	/**
	 * Launches a new {@link Activity}
	 * 
	 * @param targetActivityClass The target {@link Activity} class to launch
	 * @param requestCode If >= 0, this code will be returned in onActivityResult() when the activity exits.
	 */
	public static void launchActivity(Class<? extends Activity> targetActivityClass, int requestCode) {
		Activity currentActivity = AbstractApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != targetActivityClass) {
			Intent intent = new Intent(currentActivity, targetActivityClass);
			currentActivity.startActivityForResult(intent, requestCode);
		}
	}
	
	/**
	 * Launches a new {@link Activity}
	 * 
	 * @param targetActivityClass The target {@link Activity} class to launch
	 * @param extraName The extra name
	 * @param extraValue The extra value
	 * @param requestCode If >= 0, this code will be returned in onActivityResult() when the activity exits.
	 */
	public static void launchActivity(Class<? extends Activity> targetActivityClass, String extraName,
			Serializable extraValue, int requestCode) {
		Activity currentActivity = AbstractApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != targetActivityClass) {
			Intent intent = new Intent(currentActivity, targetActivityClass);
			intent.putExtra(extraName, extraValue);
			currentActivity.startActivityForResult(intent, requestCode);
		}
	}
	
	/**
	 * Launches a new {@link Activity}
	 * 
	 * @param targetActivityClass The target {@link Activity} class to launch
	 * @param extraName The extra name
	 * @param extraValue The extra value
	 */
	public static void launchActivity(Class<? extends Activity> targetActivityClass, String extraName,
			Serializable extraValue) {
		Activity currentActivity = AbstractApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != targetActivityClass) {
			Intent intent = new Intent(currentActivity, targetActivityClass);
			intent.putExtra(extraName, extraValue);
			currentActivity.startActivity(intent);
		}
	}
	
	/**
	 * Launches a new {@link Activity}
	 * 
	 * @param targetActivityClass The target {@link Activity} class to launch
	 * @param bundle The extra {@link Bundle}
	 */
	public static void launchActivity(Class<? extends Activity> targetActivityClass, Bundle bundle) {
		Activity currentActivity = AbstractApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != targetActivityClass) {
			Intent intent = new Intent(currentActivity, targetActivityClass);
			if (bundle != null) {
				intent.putExtras(bundle);
			}
			currentActivity.startActivity(intent);
		}
	}
	
	/**
	 * Launches a new {@link Activity}
	 * 
	 * @param targetActivityClass The target {@link Activity} class to launch
	 * @param bundle The extra {@link Bundle}
	 * @param requestCode If >= 0, this code will be returned in onActivityResult() when the activity exits.
	 */
	public static void launchActivity(Class<? extends Activity> targetActivityClass, Bundle bundle, int requestCode) {
		Activity currentActivity = AbstractApplication.get().getCurrentActivity();
		if (currentActivity.getClass() != targetActivityClass) {
			Intent intent = new Intent(currentActivity, targetActivityClass);
			intent.putExtras(bundle);
			currentActivity.startActivityForResult(intent, requestCode);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static void launchViewActivity(String uriString) {
		Activity currentActivity = AbstractApplication.get().getCurrentActivity();
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
		currentActivity.startActivity(intent);
	}
}
