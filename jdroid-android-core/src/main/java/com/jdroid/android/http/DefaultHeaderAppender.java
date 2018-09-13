package com.jdroid.android.http;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.utils.LocaleUtils;

public class DefaultHeaderAppender implements HttpServiceProcessor {

	private static final String API_VERSION_HEADER = "api-version";

	private static final String USER_AGENT_HEADER_VALUE = "android";

	public static final String USER_TOKEN_HEADER = "x-user-token";

	// TODO Review these name to unify them with Device class
	public static final String CLIENT_APP_VERSION_HEADER = "clientAppVersion";
	public static final String CLIENT_OS_VERSION_HEADER = "clientOsVersion";

	private static final DefaultHeaderAppender INSTANCE = new DefaultHeaderAppender();

	public static DefaultHeaderAppender get() {
		return INSTANCE;
	}

	@Override
	public void onInit(HttpService httpService) {
		// Do Nothing
	}

	@Override
	public void beforeExecute(HttpService httpService) {

		// User Agent header
		httpService.setUserAgent(USER_AGENT_HEADER_VALUE);

		addLanguageHeader(httpService);
		addApiVersionHeader(httpService);
		addUserTokenHeader(httpService);

		httpService.addHeader(HttpService.CONTENT_TYPE_HEADER, MimeType.JSON_UTF8);
		httpService.addHeader(HttpService.ACCEPT_HEADER, MimeType.JSON);
		httpService.addHeader(HttpService.ACCEPT_ENCODING_HEADER, HttpService.GZIP_ENCODING);

		httpService.addHeader(CLIENT_APP_VERSION_HEADER, AppUtils.getVersionCode().toString());
		httpService.addHeader(CLIENT_OS_VERSION_HEADER, AndroidUtils.getApiLevel().toString());
	}

	protected void addLanguageHeader(HttpService httpService) {
		httpService.addHeader(HttpService.ACCEPT_LANGUAGE_HEADER, LocaleUtils.getAcceptLanguage());
	}

	protected void addApiVersionHeader(HttpService httpService) {
		httpService.addHeader(API_VERSION_HEADER, AbstractApplication.get().getAppContext().getServerApiVersion());
	}

	protected void addUserTokenHeader(HttpService httpService) {
		User user = SecurityContext.get().getUser();
		if (user != null) {
			httpService.addHeader(USER_TOKEN_HEADER, user.getUserToken());
		}
	}

	@Override
	public void afterExecute(HttpService httpService, HttpResponseWrapper httpResponse) {
		// Do Nothing
	}

}
