package com.mygdx.game.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

import java.util.HashMap;
import java.util.Map;

public abstract class HttpRequest {
	private URL url;
	private HashMap<String, String> headers;
	private HttpResponseCallback success;
	private HttpRequestBuilder httpRequestBuilder;

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

	protected HttpRequest setSuccessCallback(HttpResponseCallback success) {
		this.success = success;
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
				if (success != null) {
					success.call(httpResponse.getResultAsString());
				}
			}

			@Override
			public void failed(Throwable t) {

			}

			@Override
			public void cancelled() {

			}
		});
	}
}
