package com.jdroid.android.http

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.context.SecurityContext
import com.jdroid.android.utils.AndroidUtils
import com.jdroid.android.utils.AppUtils
import com.jdroid.java.http.HttpResponseWrapper
import com.jdroid.java.http.HttpService
import com.jdroid.java.http.HttpServiceProcessor
import com.jdroid.java.http.MimeType
import com.jdroid.java.utils.LocaleUtils

open class DefaultHeaderAppender : HttpServiceProcessor {

    companion object {

        private const val API_VERSION_HEADER = "api-version"

        private const val USER_AGENT_HEADER_VALUE = "android"

        const val USER_TOKEN_HEADER = "x-user-token"

        // TODO Review these name to unify them with Device class
        const val CLIENT_APP_VERSION_HEADER = "clientAppVersion"
        const val CLIENT_OS_VERSION_HEADER = "clientOsVersion"
    }

    override fun onInit(httpService: HttpService) {
        // Do Nothing
    }

    override fun beforeExecute(httpService: HttpService) {

        // User Agent header
        httpService.setUserAgent(USER_AGENT_HEADER_VALUE)

        addLanguageHeader(httpService)
        addApiVersionHeader(httpService)
        addUserTokenHeader(httpService)

        httpService.addHeader(HttpService.CONTENT_TYPE_HEADER, MimeType.JSON_UTF8)
        httpService.addHeader(HttpService.ACCEPT_HEADER, MimeType.JSON)
        httpService.addHeader(HttpService.ACCEPT_ENCODING_HEADER, HttpService.GZIP_ENCODING)

        httpService.addHeader(CLIENT_APP_VERSION_HEADER, AppUtils.getVersionCode().toString())
        httpService.addHeader(CLIENT_OS_VERSION_HEADER, AndroidUtils.getApiLevel().toString())
    }

    protected open fun addLanguageHeader(httpService: HttpService) {
        httpService.addHeader(HttpService.ACCEPT_LANGUAGE_HEADER, LocaleUtils.getAcceptLanguage())
    }

    protected open fun addApiVersionHeader(httpService: HttpService) {
        httpService.addHeader(API_VERSION_HEADER, AbstractApplication.get().appContext.serverApiVersion)
    }

    protected open fun addUserTokenHeader(httpService: HttpService) {
        val user = SecurityContext.get().user
        if (user != null) {
            httpService.addHeader(USER_TOKEN_HEADER, user.userToken)
        }
    }

    override fun afterExecute(httpService: HttpService, httpResponse: HttpResponseWrapper) {
        // Do Nothing
    }
}
