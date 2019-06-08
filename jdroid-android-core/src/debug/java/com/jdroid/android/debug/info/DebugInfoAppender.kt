package com.jdroid.android.debug.info

import androidx.core.util.Pair

interface DebugInfoAppender {

    fun getDebugInfoProperties(): List<Pair<String, Any>>
}
