package com.jdroid.android.animation;

import android.view.View;
import android.view.animation.Animation;

public class FadeInOutAnimation extends FadeInAnimation {

	public FadeInOutAnimation(final View view, final long fadeDurationMillis, final long standByDurationMillis) {
		super(view, fadeDurationMillis);
		setAnimationListener(new DefaultAnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				view.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				Animation fadeOutAnimation = new FadeOutAnimation(view, fadeDurationMillis);
				fadeOutAnimation.setStartOffset(standByDurationMillis);
				view.startAnimation(fadeOutAnimation);
			}
		});
	}
}
