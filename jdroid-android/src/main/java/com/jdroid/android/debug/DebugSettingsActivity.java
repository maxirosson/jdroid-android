package com.jdroid.android.debug;

import android.app.FragmentTransaction;
import android.os.Bundle;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;

/**
 * Debug Settings Activity just for debug purposes. These settings are disabled on the production environment
 * 
 * @author Maxi Rosson
 */
public class DebugSettingsActivity extends AbstractFragmentActivity {
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		return R.layout.fragment_container_activity;
	}
	
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.add(R.id.fragmentContainer, new DebugSettingsFragment());
			fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.commit();
		}
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#getMenuResourceId()
	 */
	@Override
	public Integer getMenuResourceId() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractPreferenceActivity#requiresAuthentication()
	 */
	@Override
	public Boolean requiresAuthentication() {
		return false;
	}
}