package com.jdroid.android.firebase.admob;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class HouseAdBuilder {

	private OnClickListener removeAdsClickListener;

	public View build(Activity activity) {
		View view = null;
		if (RemoveAdsView.displayRemoveAdsView() && (removeAdsClickListener != null)) {
			view = new RemoveAdsView(activity);
			view.setOnClickListener(removeAdsClickListener);
		}
		return view;
	}

	public void setRemoveAdsClickListener(OnClickListener removeAdsClickListener) {
		this.removeAdsClickListener = removeAdsClickListener;
	}
}
