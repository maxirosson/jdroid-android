package com.jdroid.android.androidx.lifecycle

import com.jdroid.java.exception.AbstractException

class ApiErrorResponse<T>(val exception: AbstractException) : ApiResponse<T>()