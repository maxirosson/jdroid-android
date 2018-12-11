package com.jdroid.android.recycler;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;


public abstract class IdentifiableDiffCallback<T> extends DiffUtil.Callback {

	private List<T> oldItems;
	private List<T> newItems;

	public IdentifiableDiffCallback(RecyclerViewAdapter recyclerViewAdapter, List<T> newItems) {
		this.oldItems = com.jdroid.java.collections.Lists.newArrayList((List<T>)recyclerViewAdapter.getItems());
		this.newItems = com.jdroid.java.collections.Lists.newArrayList(newItems);
	}

	@Override
	public int getOldListSize() {
		return oldItems.size();
	}

	@Override
	public int getNewListSize() {
		return newItems.size();
	}

	@Override
	public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
		return getId(oldItems.get(oldItemPosition)).equals(getId(newItems.get(newItemPosition)));
	}

	@Override
	public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
		return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
	}

	@NonNull
	protected abstract String getId(T item);
}
