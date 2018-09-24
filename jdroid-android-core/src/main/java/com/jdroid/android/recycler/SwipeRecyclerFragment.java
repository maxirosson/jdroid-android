package com.jdroid.android.recycler;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jdroid.android.R;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.SwipeRefreshLoading;

public class SwipeRecyclerFragment extends AbstractRecyclerFragment implements SwipeRefreshLayout.OnRefreshListener {

	@Override
	public Integer getContentFragmentLayout() {
		return isCardViewDecorationEnabled() ? R.layout.jdroid_cardview_swipe_recycler_fragment : R.layout.jdroid_swipe_recycler_fragment;
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return new SwipeRefreshLoading();
	}

	@Override
	public void onRefresh() {
		// Do Nothing
	}
}
