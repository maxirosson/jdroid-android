package com.jdroid.android.animation;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * 
 * @author Maxi Rosson
 */
public class FadeOutAnimation extends AlphaAnimation {
	
	public FadeOutAnimation(final View view, long durationMillis) {
		super(1.0f, 0.0f);
		setDuration(durationMillis);
		setAnimationListener(new DefaultAnimationListener() {
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				view.setVisibility(View.INVISIBLE);
			}
		});
	}
	
}
