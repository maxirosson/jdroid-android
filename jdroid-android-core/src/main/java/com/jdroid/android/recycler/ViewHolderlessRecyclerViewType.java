package com.jdroid.android.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class ViewHolderlessRecyclerViewType<ITEM> extends RecyclerViewType<ITEM, EmptyViewHolder> {

	@Override
	public final RecyclerView.ViewHolder createViewHolderFromView(View view) {
		return new EmptyViewHolder(view);
	}

	@Override
	public final void fillHolderFromItem(ITEM item, EmptyViewHolder holder) {
		// Do nothing
	}
}