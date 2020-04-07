package com.jdroid.android.sample.exception

import com.jdroid.java.exception.ErrorCode
import com.jdroid.java.exception.ErrorCodeException
import com.jdroid.java.utils.StringUtils
import com.jdroid.java.utils.ValidationUtils

enum class AndroidErrorCode private constructor(private val resourceId: Int?, private val statusCode: Int?) : ErrorCode {

    SAMPLE_ERROR(null, 203);

    override fun getStatusCode(): String {
        return statusCode!!.toString()
    }

    override fun newErrorCodeException(vararg errorCodeParameters: Any): ErrorCodeException {
        return ErrorCodeException(this, *errorCodeParameters)
    }

    override fun newErrorCodeException(): ErrorCodeException {
        return ErrorCodeException(this)
    }

    override fun newErrorCodeException(throwable: Throwable): ErrorCodeException {
        return ErrorCodeException(this, throwable)
    }

    override fun newErrorCodeException(message: String): ErrorCodeException {
        return ErrorCodeException(this, message)
    }

    override fun getTitleResId(): Int? {
        return null
    }

    override fun getDescriptionResId(): Int? {
        return resourceId
    }

    fun validateRequired(value: String) {
        if (StringUtils.isEmpty(value)!!) {
            throw newErrorCodeException()
        }
    }

    fun validateRequired(value: Any?) {
        if (value == null) {
            throw newErrorCodeException()
        }
    }

    fun validateRequired(value: Collection<*>?) {
        if (value == null || value.isEmpty()) {
            throw newErrorCodeException()
        }
    }

    fun validatePositive(value: Int) {
        if (value <= 0) {
            throw newErrorCodeException()
        }
    }

    fun validatePositive(value: Float) {
        if (value <= 0) {
            throw newErrorCodeException()
        }
    }

    fun validateMaximum(value: Int, maximum: Int) {
        if (value > maximum) {
            throw newErrorCodeException(maximum)
        }
    }

    fun validateMinimum(value: Int, minimum: Int) {
        if (value < minimum) {
            throw newErrorCodeException(minimum)
        }
    }

    fun validateMinimumLength(value: String, minimum: Int) {
        if (value.length < minimum) {
            throw newErrorCodeException(minimum)
        }
    }

    fun validateMaximumLength(value: String, maximum: Int) {
        if (value.length > maximum) {
            throw newErrorCodeException(maximum)
        }
    }

    /**
     * Validates that the two values are equals. Assumes that no value is null.
     *
     * @param value
     * @param otherValue
     */
    fun validateEquals(value: Any?, otherValue: Any) {
        if (value != null && value != otherValue) {
            throw newErrorCodeException()
        }
    }

    /**
     * Validate that the value is an email address
     *
     * @param value
     */
    fun validateEmail(value: String) {
        if (!ValidationUtils.isValidEmail(value)) {
            throw newErrorCodeException()
        }
    }

    companion object {

        fun findByStatusCode(statusCode: String): ErrorCode? {
            var errorCode: ErrorCode? = null
            for (each in values()) {
                if (each.statusCode != null && each.statusCode.toString() == statusCode) {
                    errorCode = each
                    break
                }
            }
            return errorCode
        }
    }
}
