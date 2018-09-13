package com.jdroid.android.animation;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * {@link AnimationListener} that does nothing by default.
 */
public class DefaultAnimationListener implements AnimationListener {

	@Override
	public void onAnimationStart(Animation animation) {
		// nothing by default
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// nothing by default
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// nothing by default
	}

}
