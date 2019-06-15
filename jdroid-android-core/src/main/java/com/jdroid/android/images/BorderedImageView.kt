package com.jdroid.android.images

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.LinearLayout

import com.jdroid.android.R

class BorderedImageView : LinearLayout {

    lateinit var imageView: ImageView

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, defaultImageResId: Int, widthResId: Int, heightResId: Int) : super(context) {
        var lp = LinearLayout.LayoutParams(
            resources.getDimensionPixelSize(widthResId),
            resources.getDimensionPixelSize(heightResId)
        )
        var margin = resources.getDimensionPixelSize(R.dimen.jdroid_borderedImageViewMargin)
        lp.setMargins(margin, margin, margin, margin)
        layoutParams = lp
        setBackgroundColor(Color.WHITE)

        imageView = ImageView(getContext())
        lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        margin = resources.getDimensionPixelSize(R.dimen.jdroid_borderedImageViewPadding)
        lp.setMargins(margin, margin, margin, margin)
        imageView.layoutParams = lp
        imageView.setImageResource(defaultImageResId)
        imageView.scaleType = ScaleType.CENTER_CROP
        imageView.id = R.id.image
        addView(imageView)
    }
}
