package com.jdroid.android.sample.ui.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PaginatedGridRecyclerFragment extends PaginatedRecyclerFragment {

	@NonNull
	@Override
	protected RecyclerView.LayoutManager createLayoutManager() {
		return new GridLayoutManager(getActivity(), 3);
	}

}
