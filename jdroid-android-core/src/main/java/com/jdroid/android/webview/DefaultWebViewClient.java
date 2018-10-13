package com.jdroid.android.webview;

import android.net.http.SslError;
import androidx.annotation.NonNull;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.exception.ConnectionException;

public class DefaultWebViewClient extends WebViewClient {

	private Boolean errorReceived = false;

	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		if ((errorCode != WebViewClient.ERROR_CONNECT) && (errorCode != WebViewClient.ERROR_HOST_LOOKUP)) {
			AbstractApplication.get().getExceptionHandler().logHandledException(
				new UnexpectedException("WebView error: " + errorCode + ". " + description));
		} else {
			AbstractApplication.get().getExceptionHandler().logHandledException(new ConnectionException(description));
		}
		view.setVisibility(View.GONE);
		errorReceived = true;
	}

	@Override
	public void onReceivedSslError(WebView view, @NonNull SslErrorHandler handler, SslError error) {
		AbstractApplication.get().getExceptionHandler().logHandledException(
			new UnexpectedException("WebView Ssl error: " + error.getPrimaryError()));
		handler.cancel();
	}

	public Boolean isErrorReceived() {
		return errorReceived;
	}
}
