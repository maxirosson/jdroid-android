package com.jdroid.android.debug;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.fragment.AbstractPreferenceFragment;
import com.jdroid.java.exception.UnexpectedException;

public class PreferenceAppenderActivity extends AbstractFragmentActivity {

	public static final String APPENDER_EXTRA = "prefAppender";

	public static void startActivity(Context context, PreferencesAppender preferencesAppender) {
		Intent intent = new Intent(context, PreferenceAppenderActivity.class);
		intent.putExtra(APPENDER_EXTRA, preferencesAppender);
		context.startActivity(intent);
	}

	@Override
	public int getContentView() {
		return R.layout.jdroid_fragment_container_activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.add(R.id.fragmentContainer, createNewFragment());
			fragmentTransaction.commit();
		}
	}

	protected AbstractPreferenceFragment createNewFragment() {
		return instanceAbstractPreferenceFragment(PreferenceAppenderFragment.class, getIntent().getExtras());
	}

	private <E extends AbstractPreferenceFragment> E instanceAbstractPreferenceFragment(Class<E> fragmentClass,
			Bundle bundle) {
		E fragment;
		try {
			fragment = fragmentClass.newInstance();
		} catch (InstantiationException e) {
			throw new UnexpectedException(e);
		} catch (IllegalAccessException e) {
			throw new UnexpectedException(e);
		}
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public Integer getMenuResourceId() {
		return null;
	}

}