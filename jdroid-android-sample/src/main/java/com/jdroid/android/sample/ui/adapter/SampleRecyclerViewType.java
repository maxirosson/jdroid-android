package com.jdroid.android.sample.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.database.room.SampleEntity;

import androidx.recyclerview.widget.RecyclerView;

public abstract class SampleRecyclerViewType extends RecyclerViewType<SampleEntity, SampleRecyclerViewType.SampleHolder> {

	@Override
	protected Class<SampleEntity> getItemClass() {
		return SampleEntity.class;
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
	public void fillHolderFromItem(SampleEntity item, SampleHolder holder) {
		holder.name.setText(item.getField());
	}

	public static class SampleHolder extends RecyclerView.ViewHolder {

		protected TextView name;

		public SampleHolder(View itemView) {
			super(itemView);
		}
	}
}