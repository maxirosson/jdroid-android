package com.jdroid.android.recycler;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jdroid.android.R;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.NonBlockingLoading;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class AbstractRecyclerFragment extends AbstractFragment implements RecyclerViewContainer {

	private static final String SELECTED_ITEM_POSITION_EXTRA = "selectedItemPositionExtra";

	private RecyclerView recyclerView;
	private RecyclerViewAdapter adapter;
	protected ViewGroup emptyViewContainer;
	private RecyclerView.AdapterDataObserver adapterDataObserver;
	private RecyclerView.LayoutManager layoutManager;
	private Integer selectedItemPosition;

	@MainThread
	@Override
	public Integer getContentFragmentLayout() {
		return isCardViewDecorationEnabled() ? R.layout.jdroid_cardview_vertical_recycler_fragment : R.layout.jdroid_vertical_recycler_fragment;
	}

	@MainThread
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		recyclerView = findView(R.id.recyclerView);
		recyclerView.setHasFixedSize(hasRecyclerViewFixedSize());
		layoutManager = createLayoutManager();
		recyclerView.setLayoutManager(layoutManager);

		if (isDividerItemDecorationEnabled()) {
			recyclerView.addItemDecoration(createDividerItemDecoration());
		}

		emptyViewContainer = findView(R.id.emptyViewContainer);

		if (adapterDataObserver == null) {
			adapterDataObserver = new RecyclerView.AdapterDataObserver() {
				@Override
				public void onItemRangeInserted(int positionStart, int itemCount) {
					refreshEmptyView();
				}

				@Override
				public void onItemRangeRemoved(int positionStart, int itemCount) {
					refreshEmptyView();
				}
			};
		}
		if (adapter != null) {
			setAdapter(adapter);
		}

		if (savedInstanceState != null) {
			selectedItemPosition = savedInstanceState.getInt(SELECTED_ITEM_POSITION_EXTRA, RecyclerView.NO_POSITION);
		}
	}

	@MainThread
	protected RecyclerView.ItemDecoration createDividerItemDecoration() {
		return new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
	}

	@MainThread
	protected Boolean isDividerItemDecorationEnabled() {
		return false;
	}

	@MainThread
	protected Boolean isCardViewDecorationEnabled() {
		return false;
	}

	// use this setting to improve performance if you know that changes
	// in content do not change the layout size of the RecyclerView
	@MainThread
	protected Boolean hasRecyclerViewFixedSize() {
		return true;
	}

	@NonNull
	protected RecyclerView.LayoutManager createLayoutManager() {
		return new LinearLayoutManager(getActivity());
	}

	@MainThread
	public void setAdapter(RecyclerViewAdapter adapter) {
		this.adapter = adapter;

		adapter.registerAdapterDataObserver(adapterDataObserver);
		recyclerView.setAdapter(adapter);

		refreshEmptyView();

		if (selectedItemPosition != null) {
			this.adapter.setSelectedItemPosition(selectedItemPosition);
			selectedItemPosition = null;
		}
	}

	@MainThread
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (adapter != null && adapterDataObserver != null) {
			adapter.unregisterAdapterDataObserver(adapterDataObserver);
		}
	}

	private void refreshEmptyView() {
		if (emptyViewContainer != null) {
			int emptyViewContainerVisibility;
			if (adapter.getItemCount() == 0) {
				emptyViewContainerVisibility = View.VISIBLE;
				if (emptyViewContainer.getChildCount() == 0) {
					View emptyView = createEmptyView();
					if (emptyView != null) {
						emptyViewContainer.addView(emptyView);
					} else {
						emptyViewContainerVisibility = View.GONE;
					}
				}
			} else {
				emptyViewContainerVisibility = View.GONE;
			}
			emptyViewContainer.setVisibility(emptyViewContainerVisibility);
		}
	}

	@MainThread
	protected View createEmptyView() {
		TextView emptyTextView = (TextView)inflate(R.layout.jdroid_empty_view);
		emptyTextView.setText(getNoResultsResId());
		return emptyTextView;
	}

	protected int getNoResultsResId() {
		return R.string.jdroid_noResults;
	}

	@Override
	public RecyclerView getRecyclerView() {
		return recyclerView;
	}


	@Override
	public RecyclerViewAdapter getRecyclerViewAdapter() {
		return adapter;
	}


	public RecyclerView.LayoutManager getLayoutManager() {
		return layoutManager;
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return new NonBlockingLoading();
	}

	@MainThread
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (adapter != null) {
			outState.putSerializable(SELECTED_ITEM_POSITION_EXTRA, getRecyclerViewAdapter().getSelectedItemPosition());
		}
	}
}

