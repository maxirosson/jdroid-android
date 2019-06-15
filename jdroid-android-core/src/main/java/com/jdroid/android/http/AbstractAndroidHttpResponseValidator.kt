package com.jdroid.android.http

import com.jdroid.android.exception.CommonErrorCode
import com.jdroid.java.exception.ErrorCode
import com.jdroid.java.http.AbstractHttpResponseValidator

abstract class AbstractAndroidHttpResponseValidator : AbstractHttpResponseValidator() {

    override fun findByCommonStatusCode(statusCode: String): ErrorCode? {
        return CommonErrorCode.findByStatusCode(statusCode)
    }
}