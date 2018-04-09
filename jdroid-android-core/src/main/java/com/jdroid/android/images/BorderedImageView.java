package com.jdroid.android.images;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import com.jdroid.android.R;

public class BorderedImageView extends LinearLayout {
	
	private ImageView imageView;
	
	public BorderedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public BorderedImageView(Context context) {
		super(context);
	}
	
	public BorderedImageView(Context context, int defaultImageResId, int widthResId, int heightResId) {
		super(context);
		LayoutParams lp = new LayoutParams(getResources().getDimensionPixelSize(widthResId),
				getResources().getDimensionPixelSize(heightResId));
		int margin = getResources().getDimensionPixelSize(R.dimen.jdroid_borderedImageViewMargin);
		lp.setMargins(margin, margin, margin, margin);
		setLayoutParams(lp);
		setBackgroundColor(Color.WHITE);
		
		imageView = new ImageView(getContext());
		lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		margin = getResources().getDimensionPixelSize(R.dimen.jdroid_borderedImageViewPadding);
		lp.setMargins(margin, margin, margin, margin);
		imageView.setLayoutParams(lp);
		imageView.setImageResource(defaultImageResId);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setId(R.id.image);
		addView(imageView);
	}
	
	public ImageView getImageView() {
		return imageView;
	}
}
