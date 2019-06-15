package com.jdroid.android.view

import android.content.Context
import android.util.AttributeSet
import android.view.View

class ParallaxScrollView : NotifyingScrollView {

    private var parallaxViewContainer: View? = null

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        parallaxViewContainer?.translationY = scrollY * 0.5f
    }

    fun setParallaxViewContainer(parallaxViewContainer: View) {
        this.parallaxViewContainer = parallaxViewContainer
    }
}
