package com.jdroid.android.listener;

import java.io.Serializable;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.activity.ActivityLauncher;

/**
 * {@link OnClickListener} that launches an {@link Activity} on the onClick event
 */
public class LaunchOnClickListener implements OnClickListener {
	
	private Class<? extends Activity> targetActivityClass;
	private String extraName;
	private Serializable extraValue;
	private Integer requestCode;
	
	/**
	 * Launches the {@link AbstractApplication#getHomeActivityClass()} on the onClick event
	 */
	public LaunchOnClickListener() {
		this(null);
	}
	
	/**
	 * @param targetActivityClass The {@link Activity} {@link Class} to launch on the onClick event
	 */
	public LaunchOnClickListener(Class<? extends Activity> targetActivityClass) {
		this.targetActivityClass = targetActivityClass;
	}
	
	/**
	 * @param targetActivityClass The {@link Activity} {@link Class} to launch on the onClick event
	 * @param extraName The extra name
	 * @param extraValue The extra value
	 */
	public LaunchOnClickListener(Class<? extends Activity> targetActivityClass, String extraName,
			Serializable extraValue) {
		this.targetActivityClass = targetActivityClass;
		this.extraName = extraName;
		this.extraValue = extraValue;
	}
	
	/**
	 * @param targetActivityClass The {@link Activity} {@link Class} to launch on the onClick event
	 * @param extraName The extra name
	 * @param extraValue The extra value
	 * @param requestCode The request code for the activity to start for result
	 */
	public LaunchOnClickListener(Class<? extends Activity> targetActivityClass, String extraName,
			Serializable extraValue, int requestCode) {
		this(targetActivityClass, extraName, extraValue);
		this.requestCode = requestCode;
	}
	
	/**
	 * @param targetActivityClass The {@link Activity} {@link Class} to launch on the onClick event
	 * @param requestCode The request code for the activity to start for result
	 */
	public LaunchOnClickListener(Class<? extends Activity> targetActivityClass, int requestCode) {
		this(targetActivityClass);
		this.requestCode = requestCode;
	}
	
	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		
		if (targetActivityClass == null) {
			ActivityLauncher.launchHomeActivity();
		} else if (extraName != null) {
			if (requestCode != null) {
				ActivityLauncher.launchActivity(targetActivityClass, extraName, extraValue, requestCode);
			} else {
				ActivityLauncher.launchActivity(targetActivityClass, extraName, extraValue);
			}
		} else {
			if (requestCode != null) {
				ActivityLauncher.launchActivity(targetActivityClass, requestCode);
			} else {
				ActivityLauncher.launchActivity(targetActivityClass);
			}
		}
	}
}
