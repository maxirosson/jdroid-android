package com.jdroid.android.recycler;

public abstract class HeaderRecyclerViewType extends ViewHolderlessRecyclerViewType<HeaderRecyclerViewType.HeaderItem> {

	@Override
	protected Class<HeaderItem> getItemClass() {
		return HeaderItem.class;
	}

	@Override
	protected Boolean isClickable() {
		return false;
	}

	public static class HeaderItem {

	}
}