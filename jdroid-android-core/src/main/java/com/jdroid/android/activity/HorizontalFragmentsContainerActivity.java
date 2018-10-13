package com.jdroid.android.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.MenuItem;

import com.jdroid.android.R;
import com.jdroid.android.fragment.FragmentIf;

public abstract class HorizontalFragmentsContainerActivity extends AbstractFragmentActivity {

	@Override
	public int getContentView() {
		return isNavDrawerEnabled() ? R.layout.jdroid_nav_horizontal_fragments_container_activity : R.layout.jdroid_horizontal_fragments_container_activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null && !isFinishing()) {

			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

			Fragment leftFragment = createNewLeftFragment();
			fragmentTransaction.add(R.id.leftFragmentContainer, leftFragment, leftFragment.getClass().getSimpleName());

			Fragment rightFragment = createNewRightFragment();
			fragmentTransaction.add(R.id.rightFragmentContainer, rightFragment, rightFragment.getClass().getSimpleName());

			if (addToBackStack()) {
				fragmentTransaction.addToBackStack(rightFragment.getClass().getSimpleName());
			}
			fragmentTransaction.commit();
		}
	}

	protected Boolean addToBackStack() {
		return false;
	}

	protected Fragment createNewLeftFragment() {
		return instanceFragment(getLeftFragmentClass(), getIntent().getExtras());
	}

	protected Class<? extends Fragment> getLeftFragmentClass() {
		return null;
	}

	protected Fragment createNewRightFragment() {
		return instanceFragment(getRightFragmentClass(), getIntent().getExtras());
	}

	protected Class<? extends Fragment> getRightFragmentClass() {
		return null;
	}

	public Fragment getLeftFragment() {
		return getSupportFragmentManager().findFragmentById(R.id.leftFragmentContainer);
	}

	public Fragment getRightFragment() {
		return getSupportFragmentManager().findFragmentById(R.id.rightFragmentContainer);
	}

	@Override
	public Integer getMenuResourceId() {
		Integer menuResourceId = super.getMenuResourceId();
		if (menuResourceId == null) {
			Fragment fragment = getLeftFragment();
			if (fragment != null && fragment instanceof FragmentIf) {
				return ((FragmentIf)fragment).getMenuResourceId();
			}
		}
		return menuResourceId;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item) || getLeftFragment().onOptionsItemSelected(item) || getRightFragment().onOptionsItemSelected(item);
	}

}
