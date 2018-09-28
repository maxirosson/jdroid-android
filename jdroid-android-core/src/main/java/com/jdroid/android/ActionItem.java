package com.jdroid.android;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.Serializable;

public interface ActionItem extends Serializable {

	String getName();

	Integer getIconResource();

	Integer getNameResource();

	Integer getDescriptionResource();

	Fragment createFragment(Object args);

	void startActivity(FragmentActivity fragmentActivity);

	Intent getIntent();

	Boolean matchesActivity(FragmentActivity fragmentActivity);
}
