package com.jdroid.android.sample.ui.loading;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.SwipeRecyclerFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.adapter.SampleRecyclerViewType;
import com.jdroid.android.sample.usecase.SampleItemsUseCase;
import com.jdroid.java.utils.IdGenerator;

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
		registerUseCase(sampleItemsUseCase, this, UseCaseTrigger.ONCE);
	}

	@Override
	public void onStop() {
		super.onStop();
		unregisterUseCase(sampleItemsUseCase, this);
	}
	
	@Override
	public void onRefresh() {
		if (!sampleItemsUseCase.isInProgress()) {
			executeUseCase(sampleItemsUseCase);
		}
	}

	@Override
	public void onFinishUseCase() {
		executeOnUIThread(() -> {
            setAdapter(new RecyclerViewAdapter(new SampleRecyclerViewType() {

                @Override
                public void onItemSelected(String item, View view) {
                    getAdapter().removeItem(item);
                }

                @Override
                public AbstractRecyclerFragment getAbstractRecyclerFragment() {
                    return SwipeRefreshLoadingFragment.this;
                }
            }, sampleItemsUseCase.getItems()));
            dismissLoading();
        });
	}

	@Override
	public Integer getMenuResourceId() {
		return R.menu.recycler_menu;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.add:
				getAdapter().addItem(IdGenerator.getIntId().toString());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
