package com.jdroid.android.sample.ui.tablets;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.ScreenUtils;
import com.jdroid.android.utils.ToastUtils;

public class LeftTabletFragment extends AbstractFragment {
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.left_tablet_fragment;
	}

	@Override
	public Integer getMenuResourceId() {
		return R.menu.left_tablet_menu;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Button openDetail = findView(R.id.openDetail);
		if (ScreenUtils.is10Inches()) {
			openDetail.setVisibility(View.GONE);
		} else {
			openDetail.setVisibility(View.VISIBLE);
			openDetail.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ActivityLauncher.startActivity(getActivity(), RightTabletActivity.class);
				}
			});
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.leftAdd:
				ToastUtils.showToast(R.string.leftAction);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
