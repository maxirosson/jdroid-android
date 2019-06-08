package com.jdroid.android.sample.ui.tablets;

import android.view.MenuItem;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.ScreenUtils;
import com.jdroid.android.utils.ToastUtils;

public class RightTabletFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.right_tablet_fragment;
	}

	@Override
	public Integer getMenuResourceId() {
		return R.menu.right_tablet_menu;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.rightAdd:
				ToastUtils.showToast(R.string.rightAction);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean isSecondaryFragment() {
		return ScreenUtils.is10Inches();
	}
}
