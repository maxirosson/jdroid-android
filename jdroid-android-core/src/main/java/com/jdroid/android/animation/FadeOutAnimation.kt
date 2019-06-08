package com.jdroid.android.animation

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

class FadeOutAnimation(view: View, durationMillis: Long) : AlphaAnimation(1.0f, 0.0f) {

    init {
        duration = durationMillis
        setAnimationListener(object : DefaultAnimationListener() {

            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.INVISIBLE
            }
        })
    }
}
