package com.jdroid.android.loading;

import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;

import com.jdroid.android.R;
import com.jdroid.android.fragment.FragmentIf;

public class SwipeRefreshLoading implements FragmentLoading {

	private Integer colorRes1 = R.color.jdroid_colorPrimary;
	private Integer colorRes2 = R.color.jdroid_accentColor;
	private Integer colorRes3 = R.color.jdroid_colorPrimaryDark;
	private Integer colorRes4 = R.color.jdroid_accentColorPressed;

	private SwipeRefreshLayout swipeRefreshLayout;

	@Override
	public void onViewCreated(FragmentIf fragmentIf) {
		swipeRefreshLayout = fragmentIf.findView(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener)fragmentIf);
	}

	@Override
	public void show(FragmentIf fragmentIf) {
		if ((colorRes1 != null) && (colorRes2 != null) && (colorRes3 != null) && (colorRes4 != null)) {
			swipeRefreshLayout.setColorSchemeResources(colorRes1, colorRes2, colorRes3, colorRes4);
		}
		swipeRefreshLayout.setRefreshing(true);
	}

	@Override
	public void dismiss(FragmentIf fragmentIf) {
		swipeRefreshLayout.setRefreshing(false);
	}

	public void setColorRes1(@ColorRes Integer colorRes1) {
		this.colorRes1 = colorRes1;
	}

	public void setColorRes2(@ColorRes Integer colorRes2) {
		this.colorRes2 = colorRes2;
	}

	public void setColorRes3(@ColorRes Integer colorRes3) {
		this.colorRes3 = colorRes3;
	}

	public void setColorRes4(@ColorRes Integer colorRes4) {
		this.colorRes4 = colorRes4;
	}

}
