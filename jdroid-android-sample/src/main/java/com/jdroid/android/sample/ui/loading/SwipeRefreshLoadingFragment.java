package com.jdroid.android.sample.ui.loading;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewContainer;
import com.jdroid.android.recycler.SwipeRecyclerFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.adapter.SampleRecyclerViewType;
import com.jdroid.android.sample.usecase.SampleItemsUseCase;
import com.jdroid.android.usecase.UseCaseHelper;
import com.jdroid.android.usecase.UseCaseTrigger;
import com.jdroid.java.utils.IdGenerator;

import androidx.annotation.NonNull;


public class SwipeRefreshLoadingFragment extends SwipeRecyclerFragment {

	private SampleItemsUseCase sampleItemsUseCase;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sampleItemsUseCase = new SampleItemsUseCase();
	}

	@Override
	public void onStart() {
		super.onStart();
		UseCaseHelper.registerUseCase(sampleItemsUseCase, this, UseCaseTrigger.ONCE);
	}

	@Override
	public void onStop() {
		super.onStop();
		UseCaseHelper.unregisterUseCase(sampleItemsUseCase, this);
	}

	@Override
	public void onRefresh() {
		if (!sampleItemsUseCase.isInProgress()) {
			UseCaseHelper.executeUseCase(sampleItemsUseCase);
		}
	}

	@Override
	public void onFinishUseCase() {
		setAdapter(new RecyclerViewAdapter(new SampleRecyclerViewType() {

			@Override
			public void onItemSelected(String item, View view) {
				getRecyclerViewAdapter().removeItem(item);
			}

			@NonNull
			@Override
			public RecyclerViewContainer getRecyclerViewContainer() {
				return SwipeRefreshLoadingFragment.this;
			}
		}, sampleItemsUseCase.getItems()));
		dismissLoading();
	}

	@Override
	public Integer getMenuResourceId() {
		return R.menu.recycler_menu;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.add:
				getRecyclerViewAdapter().addItem(IdGenerator.getIntId().toString());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
