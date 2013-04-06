package com.jdroid.android.wizard;

import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @author Maxi Rosson
 */
public class WizardStepFragmentAdapter extends FragmentPagerAdapter {
	
	private List<? extends WizardStep> wizardSteps;
	private Bundle fragmentsBundle;
	
	public WizardStepFragmentAdapter(FragmentManager fm, List<? extends WizardStep> wizardSteps) {
		this(fm, wizardSteps, null);
	}
	
	public WizardStepFragmentAdapter(FragmentManager fm, List<? extends WizardStep> wizardSteps, Bundle fragmentsBundle) {
		super(fm);
		this.wizardSteps = wizardSteps;
		this.fragmentsBundle = fragmentsBundle;
	}
	
	@Override
	public Fragment getItem(int i) {
		return wizardSteps.get(i).createFragment(fragmentsBundle);
	}
	
	@Override
	public int getCount() {
		return wizardSteps.size();
	}
}