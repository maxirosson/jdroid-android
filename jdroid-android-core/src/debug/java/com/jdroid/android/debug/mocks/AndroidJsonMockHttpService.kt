package com.jdroid.android.debug.mocks

import com.jdroid.android.debug.crash.CrashGenerator
import com.jdroid.android.debug.crash.ExceptionType
import com.jdroid.android.debug.http.HttpDebugConfiguration
import com.jdroid.android.utils.SharedPreferencesHelper
import com.jdroid.java.http.mock.JsonMockHttpService

class AndroidJsonMockHttpService(vararg urlSegments: Any) : JsonMockHttpService(urlSegments) {

    companion object {
        const val HTTP_MOCK_CRASH_TYPE = "httpMockCrashType"
    }

    override fun simulateCrash() {
        val exceptionType = getHttpMockExceptionType()
        if (exceptionType != null) {
            CrashGenerator.crash(exceptionType, false)
        }
    }

    override fun getHttpMockSleepDuration(vararg urlSegments: Any): Int? {
        return HttpDebugConfiguration.getHttpMockSleepDuration()
    }

    private fun getHttpMockExceptionType(): ExceptionType? {
        return ExceptionType.find(SharedPreferencesHelper.get().loadPreference(HTTP_MOCK_CRASH_TYPE))
    }
}
