package com.jdroid.android.navdrawer;

import android.app.Activity;
import android.content.Intent;

import com.jdroid.android.activity.AbstractFragmentActivity;

import androidx.annotation.Nullable;

public class DefaultNavDrawerItem implements NavDrawerItem {

	private int itemId;
	private Class<? extends Activity> activityClass;

	public DefaultNavDrawerItem(int itemId) {
		this(itemId, null);
	}

	public DefaultNavDrawerItem(int itemId, Class<? extends Activity> activityClass) {
		this.itemId = itemId;
		this.activityClass = activityClass;
	}

	@Override
	public void startActivity(@Nullable AbstractFragmentActivity currentActivity) {
		if (currentActivity != null && currentActivity.getClass() != activityClass) {
			Intent intent = new Intent(currentActivity, activityClass);
			currentActivity.startActivity(intent);
		}
	}

	@Override
	public boolean matchesActivity(Activity activity) {
		return activity.getClass().equals(activityClass);
	}

	@Override
	public int getItemId() {
		return itemId;
	}

	@Override
	public Class<? extends Activity> getActivityClass() {
		return activityClass;
	}
}
