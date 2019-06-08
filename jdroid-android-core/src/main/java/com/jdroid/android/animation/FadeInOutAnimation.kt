package com.jdroid.android.animation

import android.view.View
import android.view.animation.Animation

class FadeInOutAnimation(view: View, fadeDurationMillis: Long, standByDurationMillis: Long) : FadeInAnimation(view, fadeDurationMillis) {

    init {
        setAnimationListener(object : DefaultAnimationListener() {

            override fun onAnimationStart(animation: Animation) {
                view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {
                val fadeOutAnimation = FadeOutAnimation(view, fadeDurationMillis)
                fadeOutAnimation.startOffset = standByDurationMillis
                view.startAnimation(fadeOutAnimation)
            }
        })
    }
}
