package com.jdroid.android.debug.info

import com.jdroid.java.collections.Lists

object DebugInfoHelper {

    private val debugInfoAppenders = Lists.newArrayList<DebugInfoAppender>()

    fun getDebugInfoAppenders(): List<DebugInfoAppender> {
        return debugInfoAppenders
    }

    fun addDebugInfoAppender(debugInfoAppender: DebugInfoAppender) {
        debugInfoAppenders.add(debugInfoAppender)
    }
}
