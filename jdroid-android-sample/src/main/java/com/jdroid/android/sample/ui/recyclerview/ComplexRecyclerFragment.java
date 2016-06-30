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
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.IdGenerator;

import java.util.List;

public class ComplexRecyclerFragment extends AbstractRecyclerFragment {

	private RecyclerViewAdapter adapter;

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
            List<RecyclerViewType> recyclerViewTypes = Lists.<RecyclerViewType>newArrayList(new StringRecyclerViewType(), new IntegerRecyclerViewType(), new BooleanRecyclerViewType());
            adapter = new RecyclerViewAdapter(recyclerViewTypes, sampleItemsUseCase.getComplexItems());
            setAdapter(adapter);
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
				adapter.addItem(IdGenerator.getIntId().toString());
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
			return ComplexRecyclerFragment.this;
		}

		@Override
		public void onItemSelected(String item, View view) {
			adapter.removeItem(item);
		}
	}

	public static class StringViewHolder extends RecyclerView.ViewHolder {

		public TextView textView;

		public StringViewHolder(View itemView) {
			super(itemView);
		}
	}

	public class IntegerRecyclerViewType extends RecyclerViewType<Integer, IntegerViewHolder> {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.item;
		}

		@Override
		protected Class<Integer> getItemClass() {
			return Integer.class;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			IntegerViewHolder viewHolder = new IntegerViewHolder(view);
			viewHolder.textView = findView(view, R.id.name);
			return viewHolder;
		}

		@Override
		public void fillHolderFromItem(Integer item, IntegerViewHolder holder) {
			holder.textView.setText(item.toString());
		}

		@Override
		protected Boolean isClickable() {
			return false;
		}

		@Override
		public AbstractRecyclerFragment getAbstractRecyclerFragment() {
			return ComplexRecyclerFragment.this;
		}
	}

	public static class IntegerViewHolder extends RecyclerView.ViewHolder {

		public TextView textView;

		public IntegerViewHolder(View itemView) {
			super(itemView);
		}
	}

	public class BooleanRecyclerViewType extends RecyclerViewType<Boolean, BooleanViewHolder> {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.item;
		}

		@Override
		protected Class<Boolean> getItemClass() {
			return Boolean.class;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			BooleanViewHolder viewHolder = new BooleanViewHolder(view);
			viewHolder.textView = findView(view, R.id.name);
			return viewHolder;
		}

		@Override
		public void fillHolderFromItem(Boolean item, BooleanViewHolder holder) {
			holder.textView.setText(item.toString());
		}

		@Override
		protected Boolean isClickable() {
			return false;
		}

		@Override
		public AbstractRecyclerFragment getAbstractRecyclerFragment() {
			return ComplexRecyclerFragment.this;
		}
	}

	public static class BooleanViewHolder extends RecyclerView.ViewHolder {

		public TextView textView;

		public BooleanViewHolder(View itemView) {
			super(itemView);
		}
	}
}
