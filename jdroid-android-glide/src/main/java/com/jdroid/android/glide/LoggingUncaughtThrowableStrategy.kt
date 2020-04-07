package com.jdroid.android.glide

import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.jdroid.android.application.AbstractApplication

class LoggingUncaughtThrowableStrategy : GlideExecutor.UncaughtThrowableStrategy {

    override fun handle(t: Throwable) {
        GlideExecutor.UncaughtThrowableStrategy.LOG.handle(t)
        AbstractApplication.get().exceptionHandler.logHandledException(t)
    }
}
