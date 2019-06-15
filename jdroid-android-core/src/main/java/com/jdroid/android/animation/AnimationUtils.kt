package com.jdroid.android.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.LayoutAnimationController
import android.view.animation.TranslateAnimation

object AnimationUtils {

    fun makeViewGroupAnimation(viewGroup: ViewGroup) {
        val set = AnimationSet(true)

        var animation: Animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 50
        set.addAnimation(animation)

        animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        animation.setDuration(150)
        set.addAnimation(animation)

        val controller = LayoutAnimationController(set, 0.5f)
        viewGroup.layoutAnimation = controller
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun showWithReveal(view: View) {

        // get the center for the clipping circle
        val cx = (view.left + view.right) / 2
        val cy = (view.top + view.bottom) / 2

        // get the final radius for the clipping circle
        val finalRadius = Math.max(view.width, view.height)

        // create the animator for this view (the start radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius.toFloat())

        // make the view visible and start the animation
        view.visibility = View.VISIBLE
        anim.duration = 1000
        anim.start()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun hideWithReveal(view: View) {

        // get the center for the clipping circle
        val cx = (view.left + view.right) / 2
        val cy = (view.top + view.bottom) / 2

        // get the initial radius for the clipping circle
        val initialRadius = view.width

        // create the animation (the final radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius.toFloat(), 0f)

        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.INVISIBLE
            }
        })

        anim.duration = 1000

        // start the animation
        anim.start()
    }
}