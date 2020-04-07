package com.jdroid.android.animation

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

open class FadeInAnimation(view: View, durationMillis: Long) : AlphaAnimation(0.0f, 1.0f) {

    init {
        duration = durationMillis
        setAnimationListener(object : DefaultAnimationListener() {

            override fun onAnimationStart(animation: Animation) {
                view.visibility = View.VISIBLE
            }
        })
    }
}
