package com.jdroid.android.firebase.admob;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.android.firebase.admob.helpers.BaseAdViewHelper;
import com.jdroid.android.recycler.RecyclerViewType;

public abstract class AdViewType extends RecyclerViewType<BaseAdViewHelper, AdViewType.AdHolder> {

	public static class AdHolder extends RecyclerView.ViewHolder {

		protected ViewGroup adViewContainer;

		public AdHolder(View itemView) {
			super(itemView);
		}
	}

	@Override
	protected Class<BaseAdViewHelper> getItemClass() {
		return BaseAdViewHelper.class;
	}

	@Override
	public RecyclerView.ViewHolder createViewHolderFromView(View view) {
		AdHolder holder = new AdHolder(view);
		holder.adViewContainer = findView(view, R.id.jdroid_recyclerAdViewContainer);
		return holder;
	}

	@Override
	public void fillHolderFromItem(BaseAdViewHelper baseAdViewHelper, AdHolder holder) {
		if (holder.adViewContainer.getChildCount() == 0) {
			baseAdViewHelper.loadAd(getActivity(), holder.adViewContainer);
		}
	}
}