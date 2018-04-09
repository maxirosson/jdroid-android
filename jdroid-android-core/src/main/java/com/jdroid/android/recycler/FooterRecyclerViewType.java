package com.jdroid.android.recycler;

public abstract class FooterRecyclerViewType extends ViewHolderlessRecyclerViewType<FooterRecyclerViewType.FooterItem> {

	@Override
	protected Class<FooterItem> getItemClass() {
		return FooterItem.class;
	}

	@Override
	protected Boolean isClickable() {
		return false;
	}

	public static class FooterItem {

	}
}