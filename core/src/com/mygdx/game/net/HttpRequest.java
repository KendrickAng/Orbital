package com.mygdx.game.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public abstract class HttpRequest {
	private URL url;
	private HashMap<String, String> headers;
	private HttpRequestBuilder httpRequestBuilder;

	private HttpResponseCallback response200;
	private HttpFailedCallback failedCallback;

	public HttpRequest(String url) {
		this.url = new URL(url);
		this.headers = new HashMap<>();
		this.httpRequestBuilder = new HttpRequestBuilder();
	}

	protected HttpRequest setURLParameter(String key, String value) {
		url.setParameter(key, value);
		return this;
	}

	protected HttpRequest setHeader(String key, String value) {
		headers.put(key, value);
		return this;
	}

	protected HttpRequest setResponse200(HttpResponseCallback success) {
		this.response200 = success;
		return this;
	}

	protected HttpRequest setFailedCallback(HttpFailedCallback failedCallback) {
		this.failedCallback = failedCallback;
		return this;
	}

	protected abstract Net.HttpRequest httpRequest(HttpRequestBuilder builder);

	public void call() {
		HttpRequestBuilder builder = httpRequestBuilder
				.newRequest()
				.url(url.toString());

		// Add Headers
		for (Map.Entry<String, String> e : headers.entrySet()) {
			builder.header(e.getKey(), e.getValue());
		}

		// Allow subclasses to modify builder
		Net.HttpRequest request = httpRequest(builder);

		// Actual Http Request
		Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				int status = httpResponse.getStatus().getStatusCode();

				switch (status) {
					case HttpStatus.SC_OK:
						if (response200 != null) {
							response200.call(httpResponse.getResultAsString());
						}
						break;
					case HttpStatus.SC_FORBIDDEN:
						// Incorrect permissions.
						// Either not authenticated, or submitted data is invalid.
						break;
					default:
						Gdx.app.log("Status", String.valueOf(status));
						break;
				}
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.log("HttpRequest.java", "Failed");
				if (failedCallback != null) {
					failedCallback.call();
				}
			}

			@Override
			public void cancelled() {

			}
		});
	}
}
