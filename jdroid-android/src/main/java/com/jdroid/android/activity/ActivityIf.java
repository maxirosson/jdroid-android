package com.jdroid.android.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import com.jdroid.android.fragment.FragmentIf;

/**
 * 
 * @author Maxi Rosson
 */
public interface ActivityIf extends FragmentIf {
	
	public Boolean onBeforeSetContentView();
	
	public void onAfterSetContentView(Bundle savedInstanceState);
	
	public int getContentView();
	
	public Integer getMenuResourceId();
	
	public void doOnCreateOptionsMenu(Menu menu);
	
	/**
	 * @return Whether this {@link Activity} requires authentication or not
	 */
	public Boolean requiresAuthentication();
	
	public MenuInflater getMenuInflater();
	
	public ActionBar getActionBar();
	
	public Boolean isLauncherActivity();
	
}
