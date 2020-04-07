package com.jdroid.android.sample.ui.glide

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView

import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.glide.GlideHelper
import com.jdroid.android.glide.LoggingRequestListener
import com.jdroid.android.sample.R

class GlideFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.glide_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageView = findView<ImageView>(R.id.image)

        findView<View>(R.id.withActivity).setOnClickListener {
            GlideHelper.with(activity).load("https://jdroidtools.com/images/mainImage.png").listener(LoggingRequestListener()).into(imageView)
        }

        findView<View>(R.id.withFragment).setOnClickListener {
            var options = RequestOptions()
            options = options.placeholder(ColorDrawable(Color.BLACK))
            GlideHelper.with(this@GlideFragment).load("https://jdroidtools.com/images/android.png").listener(LoggingRequestListener())
                .apply(options).into(imageView)
        }

        findView<View>(R.id.withApplicationContext).setOnClickListener {
            var options = RequestOptions()
            options = options.placeholder(R.drawable.jdroid_ic_about_black_24dp)
            GlideHelper.with(AbstractApplication.get()).load("https://jdroidtools.com/images/gradle.png").listener(LoggingRequestListener())
                .apply(options).into(imageView)
        }

        findView<View>(R.id.withNullContext).setOnClickListener {
            var options = RequestOptions()
            options = options.placeholder(R.drawable.jdroid_ic_about_black_24dp)
            GlideHelper.with(null as Context?).load("https://jdroidtools.com/images/gradle.png").listener(LoggingRequestListener())
                .apply(options).into(imageView)
        }

        findView<View>(R.id.invalidResourceUrl).setOnClickListener {
            var options = RequestOptions()
            options = options.placeholder(R.drawable.jdroid_ic_about_black_24dp)
            GlideHelper.with(AbstractApplication.get()).load("https://jdroidtools.com/images/invalid.png").listener(LoggingRequestListener())
                .apply(options).into(imageView)
        }

        findView<View>(R.id.outOfMemory).setOnClickListener {
            var options = RequestOptions()
            options = options.placeholder(R.drawable.jdroid_ic_about_black_24dp)
            options = options.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            GlideHelper.with(AbstractApplication.get())
                .load("https://upload.wikimedia.org/wikipedia/commons/2/2c/A_new_map_of_Great_Britain_according_to_the_newest_and_most_exact_observations_%288342715024%29.jpg")
                .listener(LoggingRequestListener()).apply(options).into(imageView)
        }
    }
}
