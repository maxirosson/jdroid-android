package com.jdroid.android.share;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;

public class MoreSharingItem extends SharingItem {

	private SharingData sharingData;

	public MoreSharingItem(@NonNull SharingData sharingData) {
		this.sharingData = sharingData;
	}

	@Override
	public void share(Activity activity) {
		ShareUtils.shareTextContent(activity, sharingData.getShareKey(), activity.getString(R.string.jdroid_share),
			sharingData.getDefaultSharingDataItem().getSubject(), sharingData.getDefaultSharingDataItem().getText());
	}

	@Override
	public Drawable getAppIcon() {
		Drawable drawable = DrawableCompat.wrap(AbstractApplication.get().getResources().getDrawable(R.drawable.jdroid_more_selector));
		DrawableCompat.setTint(drawable, AbstractApplication.get().getResources().getColor(R.color.jdroid_colorPrimary));
		return drawable;
	}

	@Override
	public Boolean isEnabled() {
		return true;
	}

	@Override
	public String getApplicationId() {
		return null;
	}
}
