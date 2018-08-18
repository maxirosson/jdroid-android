package com.jdroid.android.debug.info;

import android.annotation.SuppressLint;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.R;
import com.jdroid.android.recycler.RecyclerViewType;

public abstract class PairItemRecyclerViewType extends RecyclerViewType<Pair, PairItemRecyclerViewType.PairItemHolder> {

	@Override
	protected Class<Pair> getItemClass() {
		return Pair.class;
	}

	@Override
	protected Integer getLayoutResourceId() {
		return R.layout.jdroid_pair_item;
	}

	@Override
	public RecyclerView.ViewHolder createViewHolderFromView(View view) {
		PairItemHolder holder = new PairItemHolder(view);
		holder.name = findView(view, R.id.name);
		return holder;
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void fillHolderFromItem(Pair item, PairItemHolder holder) {
		holder.name.setText(item.first + ": " + item.second);
		holder.name.setTextIsSelectable(isTextSelectable());
	}

	protected Boolean isTextSelectable() {
		return false;
	}

	public static class PairItemHolder extends RecyclerView.ViewHolder {

		protected TextView name;

		public PairItemHolder(View itemView) {
			super(itemView);
		}
	}
}

