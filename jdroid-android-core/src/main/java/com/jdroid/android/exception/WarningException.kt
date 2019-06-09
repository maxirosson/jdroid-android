package com.jdroid.android.exception

import com.jdroid.java.exception.AbstractException

class WarningException : AbstractException {

    constructor(message: String, cause: Throwable) : super(message, cause) {
        priorityLevel = LOW_PRIORITY
    }

    constructor(message: String) : super(message) {
        priorityLevel = LOW_PRIORITY
    }

    constructor(message: String, ignoreStackTrace: Boolean) : super(message) {
        isIgnoreStackTrace = ignoreStackTrace
        priorityLevel = LOW_PRIORITY
    }
}
