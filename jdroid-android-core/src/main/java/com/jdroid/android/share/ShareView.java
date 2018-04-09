package com.jdroid.android.share;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.ScreenUtils;

import java.util.List;

public class ShareView extends FrameLayout {
	
	public ShareView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public ShareView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ShareView(Context context) {
		super(context);
	}
	
	public void init(Context context, final SharingItem sharingItem) {
		LayoutInflater.from(context).inflate(R.layout.jdroid_share_view, this, true);
		((ImageView)findViewById(R.id.shareAppIcon)).setImageDrawable(sharingItem.getAppIcon());
		
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sharingItem.share();
			}
		});
	}
	
	public static Boolean initShareSection(final Activity activity, List<SharingItem> sharingItems,
			MoreSharingItem moreSharingItem) {
		ViewGroup shareItemsContainer = activity.findViewById(R.id.shareItemsContainer);
		Boolean result = initShareSection(activity, sharingItems);
		
		if (moreSharingItem != null && result) {
			ShareView shareView = new ShareView(activity);
			shareView.init(activity, moreSharingItem);
			shareItemsContainer.addView(shareView);
		}
		return result;
	}
	
	public static Boolean initShareSectionV2(final Activity activity, List<SharingItem> sharingItems,
			@NonNull final SharingData sharingData) {
		Boolean result = initShareSection(activity, sharingItems);
		activity.findViewById(R.id.shareMore).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				ShareUtils.shareTextContent(sharingData.getShareKey(), AbstractApplication.get().getString(R.string.jdroid_share),
						sharingData.getDefaultSharingDataItem().getSubject(), sharingData.getDefaultSharingDataItem().getText());
			}
		});
		return result;
	}
	
	private static Boolean initShareSection(Activity activity, List<SharingItem> sharingItems) {
		ViewGroup shareSection = activity.findViewById(R.id.shareSection);
		ViewGroup shareItemsContainer = activity.findViewById(R.id.shareItemsContainer);
		
		int itemWidth = ScreenUtils.convertDimenToPixel(R.dimen.jdroid_shareItemWidth);
		int itemsToDisplay = (ScreenUtils.getScreenWidthPx() / itemWidth) - 1;
		for (SharingItem each : sharingItems) {
			if (each.isEnabled() && (itemsToDisplay > 0)) {
				ShareView shareView = new ShareView(activity);
				shareView.init(activity, each);
				shareItemsContainer.addView(shareView);
				
				shareSection.setVisibility(View.VISIBLE);
				
				itemsToDisplay--;
			}
		}
		return shareSection.getVisibility() == View.VISIBLE;
	}
}
