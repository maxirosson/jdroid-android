package com.jdroid.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class ParallaxScrollView extends NotifyingScrollView {

	private View parallaxViewContainer;

	public ParallaxScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ParallaxScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ParallaxScrollView(Context context) {
		super(context);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		parallaxViewContainer.setTranslationY(getScrollY() * 0.5f);
	}

	public void setParallaxViewContainer(View parallaxViewContainer) {
		this.parallaxViewContainer = parallaxViewContainer;
	}

}
