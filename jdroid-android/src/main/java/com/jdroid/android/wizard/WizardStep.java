package com.jdroid.android.wizard;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 
 * @author Maxi Rosson
 */
public interface WizardStep {
	
	public Fragment createFragment(Bundle bundle);
}
