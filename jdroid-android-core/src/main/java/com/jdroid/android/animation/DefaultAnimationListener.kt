package com.jdroid.android.animation

import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener

/**
 * [AnimationListener] that does nothing by default.
 */
open class DefaultAnimationListener : AnimationListener {

    override fun onAnimationStart(animation: Animation) {
        // nothing by default
    }

    override fun onAnimationEnd(animation: Animation) {
        // nothing by default
    }

    override fun onAnimationRepeat(animation: Animation) {
        // nothing by default
    }
}
