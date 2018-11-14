package com.jdroid.android.sample.ui.loading;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.androidx.lifecycle.ResourceObserver;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewContainer;
import com.jdroid.android.recycler.SwipeRecyclerFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.androidx.SampleListViewModel;
import com.jdroid.android.sample.androidx.SampleRepository;
import com.jdroid.android.sample.database.room.SampleEntity;
import com.jdroid.android.sample.ui.adapter.SampleRecyclerViewType;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class SwipeRefreshLoadingFragment extends SwipeRecyclerFragment {

	private SampleListViewModel sampleListViewModel;
	private Observer<Resource<List<SampleEntity>>> observer;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		observer = new ResourceObserver<List<SampleEntity>>() {

			@Override
			protected void onDataChanged(List<SampleEntity> data) {
				setAdapter(new RecyclerViewAdapter(new SampleRecyclerViewType() {

					@Override
					public void onItemSelected(SampleEntity item, View view) {
						sampleListViewModel.removeItem(item.getId());
					}

					@NonNull
					@Override
					public RecyclerViewContainer getRecyclerViewContainer() {
						return SwipeRefreshLoadingFragment.this;
					}
				}, data));
			}

			@Override
			protected void onStarting() {
				showLoading();
			}

			@Override
			protected void onStartLoading(@Nullable List<SampleEntity> data) {
				showLoading();
			}

			@Override
			protected AbstractFragment getFragment() {
				return SwipeRefreshLoadingFragment.this;
			}
		};
		sampleListViewModel = ViewModelProviders.of(this).get(SampleListViewModel.class);
		execute(false);
	}

	@Override
	public void onRefresh() {
		execute(true);
	}

	private void execute(Boolean forceRefresh) {
		if (sampleListViewModel.getSampleEntities() != null && forceRefresh) {
			sampleListViewModel.getSampleEntities().removeObserver(observer);
		}
		sampleListViewModel.load(SampleRepository.ID, forceRefresh, false).observe(this, observer);
	}

	@Override
	public Integer getMenuResourceId() {
		return R.menu.recycler_menu;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.addItem:
				sampleListViewModel.addItem();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
