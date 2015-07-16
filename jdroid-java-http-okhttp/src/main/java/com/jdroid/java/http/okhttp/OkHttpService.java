package com.jdroid.java.http.okhttp;

import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.AbstractHttpService;
import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class OkHttpService extends AbstractHttpService {

	public OkHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
	}

	@Override
	protected HttpResponseWrapper doExecute(String urlString) {

		OkHttpClient client = new OkHttpClient();
		client.setConnectTimeout(getConnectionTimeout(), TimeUnit.MILLISECONDS);
		client.setWriteTimeout(getReadTimeout(), TimeUnit.MILLISECONDS);
		client.setReadTimeout(getReadTimeout(), TimeUnit.MILLISECONDS);

		Request.Builder builder = new Request.Builder();
		builder.url(urlString);

		// Add Headers
		addHeaders(builder);

		onConfigureRequestBuilder(builder);
		Request request = builder.build();

		try {
			Response response = client.newCall(request).execute();
			return new OkHttpResponseWrapper(response);
		} catch (SocketTimeoutException e) {
			throw new ConnectionException(e, true);
		} catch (ConnectException e) {
			throw new ConnectionException(e, false);
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}

	private void addHeaders(Request.Builder builder) {
		for (Map.Entry<String, String> entry : getHeaders().entrySet()) {
			builder.addHeader(entry.getKey(), entry.getValue());
		}
	}

	protected void onConfigureRequestBuilder(Request.Builder builder) {
		// Do nothing
	}

	@Override
	protected void doFinally() {
		// Do nothing
	}
}
