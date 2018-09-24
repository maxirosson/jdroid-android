package com.jdroid.android.sample.ui.recyclerview;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.android.recycler.pagination.AbstractSearchPaginatedRecyclerFragment;
import com.jdroid.android.recycler.pagination.SearchUseCase;
import com.jdroid.android.sample.R;

import java.util.List;

public class SearchPaginatedRecyclerFragment extends AbstractSearchPaginatedRecyclerFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setThreshold(3);
	}

	@Override
	protected SearchUseCase createPaginatedUseCase() {
		return new SampleSearchUseCase();
	}

	@Override
	protected RecyclerViewAdapter createAdapter(List<Object> items) {
		return new RecyclerViewAdapter(new StringRecyclerViewType(), items);
	}

	@Override
	public boolean isSearchValueRequired() {
		return true;
	}

	@Override
	public Boolean isInstantSearchEnabled() {
		return true;
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
			return SearchPaginatedRecyclerFragment.this;
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
