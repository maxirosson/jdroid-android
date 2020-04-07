package com.jdroid.android.debug.info

object DebugInfoHelper {

    private val debugInfoAppenders = mutableListOf<DebugInfoAppender>()

    fun getDebugInfoAppenders(): List<DebugInfoAppender> {
        return debugInfoAppenders
    }

    fun addDebugInfoAppender(debugInfoAppender: DebugInfoAppender) {
        debugInfoAppenders.add(debugInfoAppender)
    }
}
