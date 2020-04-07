package com.jdroid.android.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView

open class NotifyingScrollView : NestedScrollView {

    private var onScrollChangedListener: OnScrollChangedListener? = null

    interface OnScrollChangedListener {

        fun onScrollChanged(who: NestedScrollView, l: Int, t: Int, oldl: Int, oldt: Int)
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        onScrollChangedListener?.onScrollChanged(this, l, t, oldl, oldt)
    }

    fun setOnScrollChangedListener(onScrollChangedListener: OnScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener
    }
}
