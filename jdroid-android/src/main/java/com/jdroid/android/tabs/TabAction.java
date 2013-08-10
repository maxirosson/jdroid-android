package com.jdroid.android.tabs;

import java.io.Serializable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * 
 * @author Maxi Rosson
 */
public interface TabAction extends Serializable {
	
	public String getName();
	
	public int getIconResource();
	
	public int getNameResource();
	
	public Fragment createFragment(Object args);
	
	public void startActivity(FragmentActivity fragmentActivity);
	
	public Boolean matchesActivity(FragmentActivity fragmentActivity);
}
