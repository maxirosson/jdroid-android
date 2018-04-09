package com.jdroid.android.animation;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class FadeInAnimation extends AlphaAnimation {
	
	public FadeInAnimation(final View view, long durationMillis) {
		super(0.0f, 1.0f);
		setDuration(durationMillis);
		setAnimationListener(new DefaultAnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				view.setVisibility(View.VISIBLE);
			}
		});
	}
	
}
