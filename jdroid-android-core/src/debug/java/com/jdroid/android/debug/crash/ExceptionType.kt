package com.jdroid.android.debug.crash

import com.jdroid.java.exception.CommonErrorCode
import com.jdroid.java.exception.ErrorCodeException
import com.jdroid.java.exception.UnexpectedException
import com.jdroid.java.http.exception.ConnectionException
import com.jdroid.java.http.exception.HttpResponseException

enum class ExceptionType {

    CONNECTION_EXCEPTION {
        override fun crash() {
            throw ConnectionException(CRASH_MESSAGE)
        }
    },
    ERROR_CODE_EXCEPTION {
        override fun crash() {
            throw ErrorCodeException(CommonErrorCode.UNEXPECTED_ERROR, CRASH_MESSAGE)
        }
    },
    HTTP_RESPONSE_EXCEPTION {
        override fun crash() {
            throw HttpResponseException(CRASH_MESSAGE)
        }
    },
    UNEXPECTED_EXCEPTION {
        override fun crash() {
            throw UnexpectedException(CRASH_MESSAGE)
        }
    },
    UNEXPECTED_WRAPPED_EXCEPTION {
        override fun crash() {
            try {
                val value: Long? = null
                value!!.toString()
            } catch (e: Exception) {
                throw UnexpectedException(CRASH_MESSAGE, e)
            }
        }
    },
    UNEXPECTED_NO_MESSAGE_WRAPPED_EXCEPTION {
        override fun crash() {
            try {
                val value: Long? = null
                value!!.toString()
            } catch (e: Exception) {
                throw UnexpectedException(e)
            }
        }
    },
    RUNTIME_EXCEPTION {
        override fun crash() {
            throw RuntimeException(CRASH_MESSAGE)
        }
    },
    RUNTIME_NO_MESSAGE_EXCEPTION {
        override fun crash() {
            throw RuntimeException()
        }
    };

    abstract fun crash()

    companion object {

        private const val CRASH_MESSAGE = "This is a generated crash for testing"

        fun find(name: String): ExceptionType? {
            try {
                return valueOf(name)
            } catch (e: Exception) {
                return null
            }
        }
    }
}
