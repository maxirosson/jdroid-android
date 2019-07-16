package com.jdroid.android.sample.api

import com.jdroid.android.http.AbstractAndroidHttpResponseValidator
import com.jdroid.android.sample.exception.AndroidErrorCode
import com.jdroid.java.exception.ErrorCode

object HttpResponseValidator : AbstractAndroidHttpResponseValidator() {

    override fun findByStatusCode(statusCode: String): ErrorCode? {
        return AndroidErrorCode.findByStatusCode(statusCode)
    }
}
