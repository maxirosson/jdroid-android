package com.jdroid.android.navdrawer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.jdroid.android.R;
import com.jdroid.android.images.BezelImageView;

public class NavDrawerHeaderImageView extends BezelImageView {

	public NavDrawerHeaderImageView(Context context) {
		super(context);
	}

	public NavDrawerHeaderImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NavDrawerHeaderImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void attributeInitialization(Context context, AttributeSet attrs, int defStyle) {
		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.jdroid_navDrawerHeaderImageView,
			defStyle, 0);

		Boolean circleImage = a.getBoolean(R.styleable.jdroid_navDrawerHeaderImageView_jdroid_circleImage, false);
		if (circleImage) {
			maskDrawable = context.getResources().getDrawable(R.drawable.jdroid_circle_mask);
			maskDrawable.setCallback(this);
		}

		a.recycle();

		super.attributeInitialization(context, attrs, defStyle);
	}
}
