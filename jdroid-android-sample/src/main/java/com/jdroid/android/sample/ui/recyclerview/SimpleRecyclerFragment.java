package com.jdroid.android.sample.ui.recyclerview;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.usecase.SampleItemsUseCase;
import com.jdroid.java.utils.IdGenerator;

public class SimpleRecyclerFragment extends AbstractRecyclerFragment {

	private SampleItemsUseCase sampleItemsUseCase;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sampleItemsUseCase = new SampleItemsUseCase();
	}

	@Override
	public void onStart() {
		super.onStart();
		registerUseCase(sampleItemsUseCase, this, FragmentHelper.UseCaseTrigger.ONCE);
	}

	@Override
	public void onStop() {
		super.onStop();
		unregisterUseCase(sampleItemsUseCase, this);
	}

	@Override
	public void onFinishUseCase() {
		executeOnUIThread(() -> {
            setAdapter(new RecyclerViewAdapter(new StringRecyclerViewType(), sampleItemsUseCase.getItems()));
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

	@Override
	protected Boolean isDividerItemDecorationEnabled() {
		return true;
	}

	public class StringRecyclerViewType extends RecyclerViewType<String, StringViewHolder> {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.item;
		}

		@Override
		protected Class<String> getItemClass() {
			return String.class;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			StringViewHolder viewHolder = new StringViewHolder(view);
			viewHolder.textView = findView(view, R.id.name);
			return viewHolder;
		}

		@Override
		public void fillHolderFromItem(String item, StringViewHolder holder) {
			holder.textView.setText(item);
		}

		@Override
		public AbstractRecyclerFragment getAbstractRecyclerFragment() {
			return SimpleRecyclerFragment.this;
		}

		@Override
		public void onItemSelected(String item, View view) {
			getAdapter().removeItem(item);
		}
	}

	public static class StringViewHolder extends RecyclerView.ViewHolder {

		public TextView textView;

		public StringViewHolder(View itemView) {
			super(itemView);
		}
	}
}
