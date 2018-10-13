package com.jdroid.android.navdrawer;

import android.app.Activity;
import androidx.annotation.Nullable;

import com.jdroid.android.activity.AbstractFragmentActivity;

public interface NavDrawerItem {

	public Integer getItemId();

	public void startActivity(@Nullable AbstractFragmentActivity currentActivity);

	public Boolean matchesActivity(Activity activity);

	public Class<? extends Activity> getActivityClass();

}
