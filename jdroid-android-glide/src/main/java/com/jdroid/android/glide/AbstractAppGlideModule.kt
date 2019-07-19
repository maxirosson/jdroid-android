package com.jdroid.android.glide

import android.content.Context

import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.module.AppGlideModule

open class AbstractAppGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)

        val uncaughtThrowableStrategy = LoggingUncaughtThrowableStrategy()
        builder.setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor(uncaughtThrowableStrategy))
        builder.setSourceExecutor(GlideExecutor.newSourceExecutor(uncaughtThrowableStrategy))
        // builder.setLogLevel(Log.INFO);
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}
