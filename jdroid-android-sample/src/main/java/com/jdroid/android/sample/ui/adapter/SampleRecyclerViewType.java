package com.jdroid.android.sample.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.android.sample.R;

public abstract class SampleRecyclerViewType extends RecyclerViewType<String, SampleRecyclerViewType.SampleHolder> {

	@Override
	protected Class<String> getItemClass() {
		return String.class;
	}

	@Override
	protected Integer getLayoutResourceId() {
		return R.layout.jdroid_default_item;
	}

	@Override
	public RecyclerView.ViewHolder createViewHolderFromView(View view) {
		SampleHolder holder = new SampleHolder(view);
		holder.name = findView(view, com.jdroid.android.R.id.name);
		return holder;
	}

	@Override
	public void fillHolderFromItem(String item, SampleHolder holder) {
		holder.name.setText(item);
	}

	public static class SampleHolder extends RecyclerView.ViewHolder {

		protected TextView name;

		public SampleHolder(View itemView) {
			super(itemView);
		}
	}
}