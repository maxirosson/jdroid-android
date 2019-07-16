package com.jdroid.android.sample.application

import android.content.Context

import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.jdroid.android.glide.AbstractAppGlideModule

@GlideModule
class SampleAppGlideModule : AbstractAppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
    }
}
