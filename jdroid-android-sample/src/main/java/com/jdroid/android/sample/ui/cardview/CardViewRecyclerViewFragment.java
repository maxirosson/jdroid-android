package com.jdroid.android.sample.ui.cardview;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewContainer;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.android.sample.R;
import com.jdroid.java.collections.Lists;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardViewRecyclerViewFragment extends AbstractRecyclerFragment {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		List<RecyclerViewType> recyclerViewTypes = Lists.<RecyclerViewType>newArrayList(new CardViewRecyclerViewType(), new CardViewClickableRecyclerViewType());
		setAdapter(new RecyclerViewAdapter(recyclerViewTypes, Lists.newArrayList("1", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)));
	}

	public class CardViewRecyclerViewType extends RecyclerViewType<Integer, SampleViewHolder> {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.simple_cardview;
		}

		@Override
		protected Class<Integer> getItemClass() {
			return Integer.class;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			return new SampleViewHolder(view);
		}

		@Override
		public void fillHolderFromItem(Integer item, SampleViewHolder holder) {
		}

		@NonNull
		@Override
		public RecyclerViewContainer getRecyclerViewContainer() {
			return CardViewRecyclerViewFragment.this;
		}

		@Override
		protected Boolean isClickable() {
			return false;
		}
	}

	public class CardViewClickableRecyclerViewType extends RecyclerViewType<String, SampleViewHolder> {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.simple_clickable_cardview;
		}

		@Override
		protected Class<String> getItemClass() {
			return String.class;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			return new SampleViewHolder(view);
		}

		@Override
		public void fillHolderFromItem(String item, SampleViewHolder holder) {
		}

		@NonNull
		@Override
		public RecyclerViewContainer getRecyclerViewContainer() {
			return CardViewRecyclerViewFragment.this;
		}

		@Override
		public void onItemSelected(String item, View view) {
		}
	}

	public static class SampleViewHolder extends RecyclerView.ViewHolder {

		public SampleViewHolder(View itemView) {
			super(itemView);
		}
	}
}
