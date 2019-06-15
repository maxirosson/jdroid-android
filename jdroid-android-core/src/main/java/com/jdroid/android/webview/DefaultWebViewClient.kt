package com.jdroid.android.webview

import android.net.http.SslError
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient

import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.exception.UnexpectedException
import com.jdroid.java.http.exception.ConnectionException

class DefaultWebViewClient : WebViewClient() {

    var isErrorReceived: Boolean = false
        private set

    override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
        if (errorCode != ERROR_CONNECT && errorCode != WebViewClient.ERROR_HOST_LOOKUP) {
            AbstractApplication.get().exceptionHandler.logHandledException(
                UnexpectedException("WebView error: $errorCode. $description")
            )
        } else {
            AbstractApplication.get().exceptionHandler.logHandledException(ConnectionException(description))
        }
        view.visibility = View.GONE
        isErrorReceived = true
    }

    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        AbstractApplication.get().exceptionHandler.logHandledException(
            UnexpectedException("WebView Ssl error: " + error.primaryError)
        )
        handler.cancel()
    }
}
