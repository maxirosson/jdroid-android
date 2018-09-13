package com.jdroid.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.jdroid.android.R;
import com.jdroid.android.fragment.FragmentIf;

public abstract class FragmentContainerActivity extends AbstractFragmentActivity {

	@Override
	public int getContentView() {
		return isNavDrawerEnabled() ? R.layout.jdroid_nav_fragment_container_activity : R.layout.jdroid_fragment_container_activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null && !isFinishing()) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			Fragment fragment = createNewFragment();
			fragmentTransaction.add(getFragmentContainerId(), fragment, fragment.getClass().getSimpleName());
			if (addToBackStack()) {
				fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
			}
			fragmentTransaction.commit();
		}
	}

	public int getFragmentContainerId() {
		return R.id.fragmentContainer;
	}

	public void commitFragment(Fragment fragment) {
		commitFragment(getFragmentContainerId(), fragment);
	}

	protected Boolean addToBackStack() {
		return false;
	}

	protected Fragment createNewFragment() {
		return instanceFragment(getFragmentClass(), getFragmentExtras());
	}

	protected Bundle getFragmentExtras() {
		Bundle bundle = getIntent().getExtras();
		return bundle != null ? bundle : new Bundle();
	}

	protected Class<? extends Fragment> getFragmentClass() {
		return null;
	}

	@Nullable
	public Fragment getFragment() {
		return getSupportFragmentManager().findFragmentById(getFragmentContainerId());
	}

	@Nullable
	public FragmentIf getFragmentIf() {
		Fragment fragment = getFragment();
		if (fragment != null && fragment instanceof FragmentIf) {
			return (FragmentIf)fragment;
		}
		return null;
	}

	@Override
	public Integer getMenuResourceId() {
		Integer menuResourceId = super.getMenuResourceId();
		if (menuResourceId == null) {
			Fragment fragment = getFragment();
			if (fragment != null && fragment instanceof FragmentIf) {
				return ((FragmentIf)fragment).getMenuResourceId();
			}
		}
		return menuResourceId;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Fragment fragment = getFragment();
		if (fragment != null) {
			fragment.onPrepareOptionsMenu(menu);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment fragment = getFragment();
		return super.onOptionsItemSelected(item) || (fragment != null && fragment.onOptionsItemSelected(item));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Fragment fragment = getFragment();
		if (fragment != null) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}

	@CallSuper
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		FragmentIf fragment = getFragmentIf();
		if (fragment != null) {
			fragment.onNewIntent(intent);
		}
	}

	@Override
	public Boolean onBackPressedHandled() {
		Boolean handled = super.onBackPressedHandled();
		if (!handled) {
			FragmentIf fragment = getFragmentIf();
			if (fragment != null) {
				handled = fragment.onBackPressedHandled();
			}
		}
		return handled;
	}
}
